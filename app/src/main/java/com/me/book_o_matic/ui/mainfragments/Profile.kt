package com.me.book_o_matic.ui.mainfragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.me.book_o_matic.R
import com.me.book_o_matic.firebasemodels.User
import com.me.book_o_matic.ui.LoginActivity
import com.me.book_o_matic.ui.SplashActivity
import com.me.book_o_matic.utils.Repository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class Profile:Fragment() {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var repo: Repository
    lateinit var settings: ImageView
    lateinit var pfpImg: ImageView
    lateinit var edtImg: ImageView
    lateinit var name: TextView
    lateinit var following: TextView
    lateinit var mail: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_profile,container,false)

        repo = Repository(view.context)
        settings = view.findViewById(R.id.settings)
        pfpImg = view.findViewById(R.id.profileImage)
        edtImg = view.findViewById(R.id.edimg)
        name = view.findViewById(R.id.name)
        following = view.findViewById(R.id.following)
        mail = view.findViewById(R.id.profile_mail)

        setvals()
        settings.setOnClickListener(View.OnClickListener {
            val popup = PopupMenu(view.context,settings)
            val inf: MenuInflater = popup.menuInflater
            inf.inflate(R.menu.settings, popup.menu)
            popup.show()
            popup.setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.logout -> {
                        auth.signOut()
                        repo.signout()
                        val intent = Intent(this.activity,SplashActivity::class.java)
                        intent.putExtra("goto",0)
                        startActivity(intent)
                        true
                    }
                    R.id.delete -> {
                        TODO()
                    }
                    else -> TODO()

                }
            }
        })
        edtImg.setOnClickListener(View.OnClickListener {
            takeImage()
        })
        return view
    }

    private fun setvals() {
        lifecycleScope.async{
            name.text = repo.getName()+"("+repo.getpenName()+")"
            following.text = "Following: ${repo.getuserSubs().split(",").size}"
            mail.text = auth.currentUser.email
            getProfileImage(auth.currentUser.photoUrl)
        }
    }
    private fun takeImage(){
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Select Image"), 101)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode === Activity.RESULT_OK) {
            if (requestCode === 101) {
                // Get the url of the image from data
                val selectedImageUri: Uri? = data?.data
                if (null != selectedImageUri) {
                    uploadImage(selectedImageUri)
                    return
                }
            }
        }
        Toast.makeText(this.activity,"There Seems to be an error Please try again", Toast.LENGTH_SHORT).show()
    }

    private fun uploadImage(file: Uri) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference
        val riversRef = storageRef.child("images/${file.lastPathSegment}")
        val uploadTask = riversRef.putFile(file)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            riversRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val user = auth.currentUser
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setPhotoUri(downloadUri)
                    .build()
                db.collection("User").document(repo.getuserID()).update("profileimage",downloadUri.toString())
                user.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(OnCompleteListener<Void?> { mtask ->
                        if (mtask.isSuccessful) {
                            getProfileImage(downloadUri)
                        }
                    })
            } else {
                // Handle failures
                // ...
            }
        }


    }

    private fun getProfileImage(downloadUri: Uri?) {
        if(downloadUri == null){
            Glide.with(this).load(R.drawable.a_books_pfp).into(pfpImg)}
        else {
            Glide.with(this).load(downloadUri).into(pfpImg)
        }
    }
}
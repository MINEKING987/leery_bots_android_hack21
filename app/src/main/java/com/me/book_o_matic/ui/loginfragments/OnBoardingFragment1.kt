package com.me.book_o_matic.ui.loginfragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.me.book_o_matic.R
import com.me.book_o_matic.firebasemodels.Index
import com.me.book_o_matic.firebasemodels.User
import com.me.book_o_matic.ui.LoginActivity


class OnBoardingFragment1() : Fragment() {
    lateinit var button: Button
    lateinit var editImg: ImageView
    lateinit var profileImg: ImageView
    lateinit var name:EditText
    lateinit var penName:EditText
     val db:FirebaseFirestore = FirebaseFirestore.getInstance()
     val auth:FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_on_boarding1, container, false)

        name = view.findViewById(R.id.edt_onBoard1_name)
        penName = view.findViewById(R.id.edt_onBoard1_PenName)
        profileImg = view.findViewById(R.id.profileImage_onBoard)
        getProfileImage(auth.currentUser.photoUrl)

        button = view.findViewById(R.id.btn_onboard1)
        editImg = view.findViewById(R.id.edimg_onBoard1)
        editImg.setOnClickListener(View.OnClickListener {
            takeImage()
        })
        button.setOnClickListener(View.OnClickListener {
            CheckandCreateUser()
        })
        return view
    }

    private fun takeImage(){
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Select Image"), 101)
    }
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)

         if (resultCode === RESULT_OK) {
             if (requestCode === 101) {
                 // Get the url of the image from data
                 val selectedImageUri: Uri? = data?.data
                 if (null != selectedImageUri) {
                        uploadImage(selectedImageUri)
                     return
                 }
             }
         }
         Toast.makeText(this.activity,"There Seems to be an error, please try again",Toast.LENGTH_SHORT).show()
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

                user.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                        if (task.isSuccessful) {
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
        if(downloadUri == null){Glide.with(this).load(R.drawable.a_books_pfp).into(profileImg)}
        else {
            Glide.with(this).load(downloadUri).into(profileImg)
        }
    }
    private fun CheckandCreateUser() {


        val userModel = User(name.text.toString(),penName.text.toString(),profileimage = auth.currentUser.photoUrl.toString())
        val batch: WriteBatch = db.batch()
        val user = db.collection("User")
        val userRef = user.document("${auth.uid}")
        batch.set(userRef,userModel)

        val indexModel= Index(userRef.id)
        val index = db.collection("Index")
        val indexRef = index.document("User/username/${penName.text}")
        batch.set(indexRef,indexModel)
        batch.commit().addOnCompleteListener {
            if(it.isSuccessful){
                (activity as LoginActivity).movetofragment(2)
                Log.i("onboarding1","Moving to 2")
            }
            else if(it.exception?.message.equals("PERMISSION_DENIED: The caller does not have permission")){
                Toast.makeText(this.activity, "PenName already in use",Toast.LENGTH_LONG).show()
            }
            else{Toast.makeText(this.activity, it.exception?.message,Toast.LENGTH_LONG).show()
                it.exception?.message?.let { it1 -> Log.i("FireBaseException:", it1) }
            }
        }
    }
}
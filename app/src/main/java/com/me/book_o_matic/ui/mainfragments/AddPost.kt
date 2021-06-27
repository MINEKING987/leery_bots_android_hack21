package com.me.book_o_matic.ui.mainfragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.me.book_o_matic.R
import com.me.book_o_matic.firebasemodels.Post
import com.me.book_o_matic.ui.LoginActivity
import com.me.book_o_matic.ui.MainActivity
import com.me.book_o_matic.utils.Repository
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class AddPost:Fragment() {

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var repo: Repository
    lateinit var post: Button
    lateinit var addimg: TextView
    lateinit var tags: EditText
    lateinit var content: EditText
    lateinit var imgview: ImageView
    lateinit var timestamp: Timestamp
    var imgurl:String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_addpost,container,false)
        repo = Repository(view.context)
        imgview = view.findViewById(R.id.appCompatImageView2)
        post = view.findViewById(R.id.postbtn)
        content = view.findViewById(R.id.content)
        tags = view.findViewById(R.id.tags)
        post.setOnClickListener(View.OnClickListener {
            postdata(view)
        })
        addimg = view.findViewById(R.id.add_img)
        addimg.setOnClickListener(View.OnClickListener {
            getimg(view)
        })
        return view
    }

    private fun postdata(view: View) {
        val post:Post = Post(repo.getuserID(),repo.getpenName()
        ,content.text.toString(),imgurl,0L,tags.text.toString(), Timestamp.now())
        db.collection("Posts").add(post).addOnCompleteListener {
            Toast.makeText(this.activity,"The world will Know....", Toast.LENGTH_SHORT).show()
            sendcloudnotif(view)
            imgview.setImageResource(0)
            tags.text="" as Editable
            content.text="" as Editable
            (activity as MainActivity).movetofragment(1)
        }

    }

    private fun sendcloudnotif(view:View) {
        val mRequestQue = Volley.newRequestQueue(view.context)

        val json = JSONObject()
        try {
            json.put("to", "/topics/" + "${repo.getuserID()}")
            val notificationObj = JSONObject()
            notificationObj.put("title", "New Post")
            notificationObj.put("body", "New Post from : " +repo.getpenName())
            //replace notification with data when went send data
            json.put("notification", notificationObj)
            val URL = "https://fcm.googleapis.com/fcm/send"
            val request: JsonObjectRequest = object : JsonObjectRequest(
                Request.Method.POST, URL,
                json,
                Response.Listener { response: JSONObject? ->
                    Log.d(
                        "MUR",
                        "onResponse: "
                    )
                },
                Response.ErrorListener { error: VolleyError ->
                    Log.d(
                        "MUR",
                        "onError: " + error.networkResponse
                    )
                }
            ) {
                override fun getHeaders(): Map<String, String> {
                    val header: MutableMap<String, String> = HashMap()
                    header["content-type"] = "application/json"
                    header["authorization"] = "key=AAAAeNQI0JY:APA91bHYk40Yto7tuQ_I7ouFH9pD0zQI0m78ELQ1tlXDyF7FDXpCfdNBq9nlq9KGlyiejMLb6Uo75lLAtuzPGTD6ot9qHf9xxOdpb8I-1daLEGA-IDy_e0fpmfsW4IJPP8Gyy3l8VLNq "
                    return header
                }
            }
            mRequestQue.add(request)
        } catch (ex: JSONException) {
            ex.printStackTrace()
            Log.e("notifexception",ex.toString())
        }
    }

    private fun getimg(view: View) {
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
            riversRef.downloadUrl.addOnCompleteListener {
                if(it.isSuccessful) {
                    Glide.with(this).load(it.result).into(imgview)
                    imgurl = it.result.toString()
                }
            }
        }
        }


    }

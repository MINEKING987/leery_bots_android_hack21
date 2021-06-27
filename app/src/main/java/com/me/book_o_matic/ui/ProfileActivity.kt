package com.me.book_o_matic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.me.book_o_matic.R
import com.me.book_o_matic.firebasemodels.User
import com.me.book_o_matic.utils.Repository

class ProfileActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    val cloudMessage = FirebaseMessaging.getInstance()
    lateinit var img:ImageView
    lateinit var btn:Button
    lateinit var repo:Repository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        repo = Repository(this)
        img = findViewById(R.id.profileImage)
        val tcxt:TextView = findViewById(R.id.profileact_name)

        val uid = intent.extras?.getString("uid")
        val name :String?= intent.extras?.getString("name")
        tcxt.text = name
        btn = findViewById(R.id.subscribe)
        btn.isEnabled = false
        if(uid != null){
            db.collection("User").document("$uid").get().addOnSuccessListener { documentSnapshot ->
                val doc = documentSnapshot.toObject(User::class.java)
                getProfileImage(doc?.profileimage ?:"")
                btn.isEnabled = true
                var subs: MutableList<String> = ArrayList<String>(repo.getuserSubs().split(","))
                Log.i("profileActivity", "$subs $uid")
                if (subs.contains("$uid")) {
                    btn.text = "UnFollow"
                }
                btn.setOnClickListener {
                    btn.isEnabled = false
                    if (subs.contains("$uid")) {
                        subs.removeAll { it == "$uid" }
                        val outstr = if(subs.isEmpty()){""}else{subs.joinToString(separator = ",")}
                        Log.i("profileActivityrem",outstr)
                        repo.setuserSubs(outstr)
                        db.collection("User").document(repo.getuserID())
                            .update("subs", outstr)
                            .addOnSuccessListener { btn.text = "Follow"
                                btn.isEnabled = true
                                cloudMessage.unsubscribeFromTopic(uid)}

                    }
                    else {
                        btn.isEnabled = false
                        subs.removeAll { it == "" }
                        subs += uid
                        Log.i("profileActivityadd", subs.joinToString(separator = ","))
                        repo.setuserSubs(subs.joinToString(separator = ","))
                        db.collection("User").document(repo.getuserID())
                            .update("subs", subs.joinToString(separator = ","))
                            .addOnSuccessListener { btn.text = "UnFollow"
                                btn.isEnabled = true
                                cloudMessage.subscribeToTopic(uid)}


                    }
                }

            }
        }
        }
    private fun getProfileImage(downloadUri: String?) {
        downloadUri?.let {
            val imgUri = it.toUri().buildUpon().scheme("https").build()
            Glide.with(this)
                .load(imgUri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo))
                .into(img)
        }
    }

    }

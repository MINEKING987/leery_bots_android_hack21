package com.me.book_o_matic.firebasemodels

import com.google.firebase.Timestamp

data class Post(
    val uid:String="",
    val username:String="",
    val content:String="",
    val photourl:String="",
    val likes:Long=0L,
    val types:String = "",
    val timestamp:Timestamp = Timestamp(0,0)
)

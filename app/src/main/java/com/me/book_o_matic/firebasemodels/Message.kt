package com.me.book_o_matic.firebasemodels

import com.google.firebase.Timestamp
import java.util.*


data class Message(
    val text:String,
    val user:String,
    val timestamp: Timestamp
)
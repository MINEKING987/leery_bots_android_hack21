package com.me.book_o_matic.adapters

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.me.book_o_matic.ui.LoginActivity
import com.me.book_o_matic.ui.MainActivity

class mMessagingservice:FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if(remoteMessage.data.isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("goto",1)
            startActivity(intent)
        }

        }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}
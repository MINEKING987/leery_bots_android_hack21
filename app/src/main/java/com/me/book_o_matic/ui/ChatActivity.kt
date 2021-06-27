package com.me.book_o_matic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.me.book_o_matic.R
import com.me.book_o_matic.adapters.ChatAdapter
import com.me.book_o_matic.firebasemodels.Message
import com.me.book_o_matic.utils.Repository
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val firestore = FirebaseFirestore.getInstance()
    lateinit var repository: Repository

    val chatMessages = ArrayList<Message>()
    var chatRegistration: ListenerRegistration? = null
    var roomId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        repository = Repository(this)
        initList()
        val title = intent.getStringExtra("title")
        val titletext:TextView=findViewById(R.id.title)
        titletext.text = title
        setViewListeners()
    }

    private fun setViewListeners() {
        button_send.setOnClickListener {
            sendChatMessage()
        }
    }

    private fun initList() {
        if (user == null)
            return
        list_chat.layoutManager = LinearLayoutManager(this)
        val adapter = ChatAdapter(chatMessages, repository.getpenName())
        list_chat.adapter = adapter
        listenForChatMessages()
    }
    private fun listenForChatMessages() {
        roomId = intent.getStringExtra("roomid")
        if (roomId == null) {
            finish()
            return
        }
        chatRegistration = firestore.collection("rooms")
            .document(roomId!!)
            .collection("messages")
            .addSnapshotListener { messageSnapshot, exception ->

                if (messageSnapshot == null || messageSnapshot.isEmpty)
                    return@addSnapshotListener

                chatMessages.clear()

                for (messageDocument in messageSnapshot.documents) {
                    chatMessages.add(
                        Message(
                            messageDocument["text"] as String,
                            messageDocument["user"] as String,
                            messageDocument["timestamp"] as Timestamp
                        ))
                }

                chatMessages.sortBy { it.timestamp }
                list_chat.adapter?.notifyDataSetChanged()
            }
    }

    private fun sendChatMessage() {
        val message = edittext_chat.text.toString()
        edittext_chat.setText("")

        firestore.collection("rooms").document(roomId!!).collection("messages")
            .add(mapOf(
                Pair("text", message),
                Pair("user", repository.getpenName()),
                Pair("timestamp", Timestamp.now())
            ))
    }


    override fun onDestroy() {
        chatRegistration?.remove()
        super.onDestroy()
    }
}

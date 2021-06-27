package com.me.book_o_matic.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.me.book_o_matic.R
import com.me.book_o_matic.firebasemodels.Message
import kotlinx.android.synthetic.main.list_item_chat.view.*


class ChatAdapter(val chatMessages: List<Message>, val user: String): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }
    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        Log.i("ChatAdapter1",chatMessage.user)
        Log.i("ChatAdapter2",user)
        if (chatMessage.user == user) {
            holder.itemView.textview_chat_sent.text = chatMessage.text
            holder.itemView.textview_chat_received.visibility = View.GONE
            holder.itemView.name.visibility = View.GONE

        } else {
            holder.itemView.name.text=chatMessage.user
            holder.itemView.textview_chat_received.text = chatMessage.text
            holder.itemView.textview_chat_sent.visibility = View.GONE
        }
    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_chat, parent, false)
    ) {

        private var chatTextSent: TextView? = null
        private var chatTextReceived: TextView? = null

        init {
            chatTextSent = itemView.findViewById(R.id.textview_chat_sent)
            chatTextReceived = itemView.findViewById(R.id.textview_chat_received)
        }

    }
}

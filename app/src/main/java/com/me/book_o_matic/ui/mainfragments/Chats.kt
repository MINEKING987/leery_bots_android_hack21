package com.me.book_o_matic.ui.mainfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.me.book_o_matic.ui.ChatActivity
import com.me.book_o_matic.R

class Chats:Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_chats,container,false)
        val general:Button = view.findViewById(R.id.general)
        general.setOnClickListener {
            val intent = Intent(this@Chats.context, ChatActivity::class.java)
            intent.putExtra("roomid","generalchatroom")
            intent.putExtra("title","General")
            startActivity(intent)
        }
        val hidden:Button = view.findViewById(R.id.hiddenjewls)
        hidden.setOnClickListener {
            val intent = Intent(this@Chats.context, ChatActivity::class.java)
            intent.putExtra("roomid","hiddenjwlsroom")
            intent.putExtra("title","Hidden Jewels")
            startActivity(intent)
        }
        val bookreco:Button = view.findViewById(R.id.bookrec)
        bookreco.setOnClickListener {
            val intent = Intent(this@Chats.context, ChatActivity::class.java)
            intent.putExtra("roomid","bookrecchatroom")
            intent.putExtra("title","Book Recommondations")
            startActivity(intent)
        }
        val webtoons:Button = view.findViewById(R.id.webtoon)
        webtoons.setOnClickListener {
            val intent = Intent(this@Chats.context, ChatActivity::class.java)
            intent.putExtra("roomid","webtoonsroom")
            intent.putExtra("title","Web Toons")
            startActivity(intent)
        }
        return view
    }
}
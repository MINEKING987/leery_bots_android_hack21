package com.me.book_o_matic.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.me.book_o_matic.R
import com.me.book_o_matic.databinding.ModelPostBinding
import com.me.book_o_matic.firebasemodels.Post
import com.me.book_o_matic.ui.ProfileActivity


class PostsAdapter : PagingDataAdapter<Post, PostsAdapter.ViewHolder>(Companion) {


    companion object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

/*    fun getCount(): Int {
        return itemCount
    }*/
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding as ModelPostBinding
        binding.viewmodel = getItem(position) ?: return
        binding.username.setOnClickListener{
            val intent =  Intent(it.context, ProfileActivity::class.java)
            intent.putExtra("uid",getItem(position)?.uid)
            intent.putExtra("name",getItem(position)?.username)
            it.context.startActivity(intent)
        }
        binding.executePendingBindings()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ModelPostBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(
        val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root)

}








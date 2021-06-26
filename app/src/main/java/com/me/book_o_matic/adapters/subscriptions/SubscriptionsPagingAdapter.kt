package com.me.book_o_matic.adapters.subscriptions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.me.book_o_matic.R
import com.me.book_o_matic.firebasemodels.Post


open class SubscriptionsPagingAdapter(@NonNull options: FirestorePagingOptions<Post?>) :
    FirestorePagingAdapter<Post, SubscriptionsPagingAdapter.ViewHolder>(options) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Post) {
        holder.name.text = model.username
        holder.data.text = model.content
        Glide.with(holder.img.context).load(model.photourl).into(holder.img)
        holder.like.setOnClickListener(View.OnClickListener {
            TODO("ADD LIKE")
        })
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.model_post,
            parent, false
        )
        return ViewHolder(v)
    }
     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView
        var name: TextView
        var data:TextView
        var like:ImageView

        init {
            img = itemView.findViewById(R.id.image)
            name = itemView.findViewById(R.id.username)
            data = itemView.findViewById(R.id.content)
            like = itemView.findViewById(R.id.like)


        }
    }

}
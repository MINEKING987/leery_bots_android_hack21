package com.me.book_o_matic.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.me.book_o_matic.R

object BindingAdapters {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun setImageUrl(imgView: ImageView, imgUrl: String?){
        imgUrl?.let {
            val imgUri = it.toUri().buildUpon().scheme("https").build()
            Glide.with(imgView.context)
                .load(imgUri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo))
                .into(imgView)
        }
    }
//    @BindingAdapter("likes")
//    @JvmStatic
//    fun setlikes(txtView: TextView, likes:Long){
//        txtView.text = likes.toString()+"  "
//        }
    }
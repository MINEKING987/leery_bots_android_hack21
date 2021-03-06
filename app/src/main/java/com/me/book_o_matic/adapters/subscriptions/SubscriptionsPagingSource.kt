package com.me.book_o_matic.adapters.subscriptions

import android.util.Log
import androidx.paging.PagingSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.me.book_o_matic.firebasemodels.Post
import com.me.book_o_matic.utils.Repository
import kotlinx.coroutines.tasks.await

class SubscriptionsPagingSource(private val db: FirebaseFirestore, subs:List<String>) : PagingSource<QuerySnapshot, Post>() {

    val subs = subs
    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> {
        return try {
            val currentPage = params.key ?: db.collection("Posts")
                .whereIn("uid",subs)
                .orderBy("timestamp")
                .limit(10).get().await()

            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]
            val nextPage = db.collection("Posts")
                .whereIn("uid",subs)
                .orderBy("timestamp")
                .limit(10).startAfter(lastDocumentSnapshot)
                .get()
                .await()

            LoadResult.Page(
                data = currentPage.toObjects(Post::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            Log.e("subspagingSource:",e.message.toString())
            LoadResult.Error(e)

        }
    }
}

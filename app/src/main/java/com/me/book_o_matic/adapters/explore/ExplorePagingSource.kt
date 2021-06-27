package com.me.book_o_matic.adapters.explore

import android.util.Log
import androidx.paging.PagingSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.me.book_o_matic.firebasemodels.Post
import com.me.book_o_matic.utils.Repository
import kotlinx.coroutines.tasks.await

class ExplorePagingSource(private val db: FirebaseFirestore, repository: Repository) : PagingSource<QuerySnapshot, Post>() {

    val repository = repository
    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> {
        return try {
            val currentPage = params.key ?: db.collection("Posts")
                .orderBy("timestamp")
                .limit(10).get().await()

            val lastDocumentSnapshot = currentPage.documents[currentPage.size() - 1]
            val nextPage = db.collection("Posts")
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
            Log.e("explorepagingSource:",e.message.toString())
            LoadResult.Error(e)

        }
    }
}

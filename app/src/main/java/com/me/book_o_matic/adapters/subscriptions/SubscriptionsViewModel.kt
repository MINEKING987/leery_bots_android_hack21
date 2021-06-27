package com.me.book_o_matic.adapters.subscriptions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.google.firebase.firestore.FirebaseFirestore
import com.me.book_o_matic.utils.Repository


class SubscriptionsViewModel(application: Application) : AndroidViewModel(application) {
    val repo = Repository(application)
    val flow = Pager(PagingConfig(20)) {
        val subs = repo.getuserSubs().split(",")
        SubscriptionsPagingSource(FirebaseFirestore.getInstance(),subs)
    }.flow.cachedIn(viewModelScope)

}

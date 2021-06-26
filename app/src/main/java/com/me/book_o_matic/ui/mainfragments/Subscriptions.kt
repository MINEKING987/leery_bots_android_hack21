package com.me.book_o_matic.ui.mainfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.me.book_o_matic.R
import com.me.book_o_matic.adapters.subscriptions.SubscriptionsViewModel
import com.me.book_o_matic.adapters.PostsAdapter
import com.me.book_o_matic.utils.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class Subscriptions:Fragment() {
    private val PostViewModel by viewModels<SubscriptionsViewModel>()
    private val postsAdapter = PostsAdapter()
    lateinit var repo: Repository
    lateinit var recyclerView: RecyclerView
    lateinit var noView: TextView
    lateinit var progressBar: ProgressBar
    lateinit var refreshLayout: SwipeRefreshLayout
    var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_subscriptions,container,false)
        noView = view.findViewById(R.id.no_views)
        progressBar = view.findViewById(R.id.prog_bar)
        refreshLayout = view.findViewById(R.id.ref_layout)
        refreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            startandrestartjob()
        })
        repo = Repository(view.context)
        recyclerView = view.findViewById(R.id.subs_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
/*        val baseQuery: Query = postsref
            .whereIn("uid",subs.split(","))
            .orderBy("timestamp",Query.Direction.ASCENDING)
        val config: PagedList.Config = PagedList.Config.Builder()
            .setPrefetchDistance(10)
            .setPageSize(3)
            .build()
        val options = FirestorePagingOptions.Builder<Post>()
            .setLifecycleOwner(this)
            .setQuery(baseQuery, config, Post::class.java)
            .build()*/
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.adapter = postsAdapter
        startandrestartjob()
        super.onViewCreated(view, savedInstanceState)
    }
    fun startandrestartjob(){
        job?.cancel()
        job = viewLifecycleOwner.lifecycleScope.launch {
            PostViewModel.flow.collect{
                postsAdapter.submitData(lifecycle,it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            postsAdapter.loadStateFlow.collectLatest { loadStates ->
                progressBar.isVisible = loadStates.refresh is LoadState.Loading

            }
        if(postsAdapter.itemCount == 0){
            noView.visibility = GONE
        }
            else{
                noView.visibility = VISIBLE
            }
        }
    }

}
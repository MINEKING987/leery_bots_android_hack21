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
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.me.book_o_matic.R
import com.me.book_o_matic.adapters.subscriptions.SubscriptionsViewModel
import com.me.book_o_matic.adapters.PostsAdapter
import com.me.book_o_matic.utils.Repository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest


class Subscriptions:Fragment() {
    val PostViewModel by viewModels<SubscriptionsViewModel>()
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
        noView.visibility = GONE

        progressBar = view.findViewById(R.id.prog_bar)
        refreshLayout = view.findViewById(R.id.ref_layout)
        refreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            postsAdapter.refresh()
            refreshLayout.isRefreshing = false
            lifecycleScope.async {
                delay(1000)
                remover()
            }
        })

        repo = Repository(view.context)
        recyclerView = view.findViewById(R.id.subs_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.adapter = postsAdapter
        startjob()
        lifecycleScope.async {
            delay(1000)
            remover()
        }
        super.onViewCreated(view, savedInstanceState)
    }
    fun startjob(){
        job = viewLifecycleOwner.lifecycleScope.launch {
            PostViewModel.flow.collect{
                postsAdapter.submitData(lifecycle,it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            postsAdapter.loadStateFlow.collectLatest { loadStates ->
                progressBar.isVisible = loadStates.refresh is LoadState.Loading

            }
        }
    }
    fun remover(){
        if(postsAdapter.itemCount == 0){
            noView.visibility = VISIBLE
        }
        else{
            noView.visibility = GONE
        }
    }

}
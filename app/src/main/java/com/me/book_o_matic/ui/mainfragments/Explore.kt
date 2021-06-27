package com.me.book_o_matic.ui.mainfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.me.book_o_matic.R
import com.me.book_o_matic.adapters.PostsAdapter
import com.me.book_o_matic.adapters.explore.ExploreViewModel
import com.me.book_o_matic.firebasemodels.Post
import com.me.book_o_matic.utils.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Explore:Fragment() {
    private val PostViewModel by viewModels<ExploreViewModel>()
    private val postsAdapter = PostsAdapter()

    lateinit var repo: Repository
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var refreshLayout: SwipeRefreshLayout
    var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_explore,container,false)
        progressBar = view.findViewById(R.id.prog_bar)
        refreshLayout = view.findViewById(R.id.ref_layout)
        refreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            startandrestartjob()
        })
        repo = Repository(view.context)
        recyclerView = view.findViewById(R.id.subs_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

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

        }
        refreshLayout.isRefreshing = false
    }
}
package com.me.book_o_matic.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.me.book_o_matic.R
import com.me.book_o_matic.adapters.GateTransformation
import com.me.book_o_matic.ui.loginfragments.LoginFragment
import com.me.book_o_matic.ui.loginfragments.OnBoardingFragment1
import com.me.book_o_matic.ui.loginfragments.OnBoardingFragment2
import com.me.book_o_matic.ui.mainfragments.*

class MainActivity : AppCompatActivity() {
    lateinit var viewPager:ViewPager2
    val titles = arrayOf("Explore","Subscriptions","Add Post","Chats","Profile")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.main_viewpager)
        run()
    }
    override fun onBackPressed() {
        if (viewPager.getCurrentItem() === 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    private fun run() {
        val tabLayout:TabLayout = findViewById(R.id.tabLayout)
        viewPager.adapter = ViewPagerFragmentAdapter(this)
        viewPager.setPageTransformer(GateTransformation())
        // attaching tab mediator
        TabLayoutMediator(tabLayout, viewPager
        ) { tab, position ->
            tab.text = titles[position]
            }.attach()
    }

    private class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        val context: Context = fragmentActivity

        //creating fragments to show when selected or slid to
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return Explore()
                1 -> return Subscriptions()
                2 -> return AddPost()
                3 -> return Chats()
                4 -> return Profile()
            }
            return LoginFragment()
        }

        override fun getItemCount(): Int {
            return 5
        }
    }
}
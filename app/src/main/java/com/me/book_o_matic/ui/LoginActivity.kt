package com.me.book_o_matic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.me.book_o_matic.R
import com.me.book_o_matic.adapters.GateTransformation
import com.me.book_o_matic.ui.loginfragments.LoginFragment
import com.me.book_o_matic.ui.loginfragments.OnBoardingFragment1
import com.me.book_o_matic.ui.loginfragments.OnBoardingFragment2
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager2
    private var goto: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        goto = intent.getIntExtra("goto",0)
        viewPager = findViewById(R.id.login_viewpager)
        viewPager.isUserInputEnabled =false
        run()
        movetofragment(goto!!)
    }
    fun movetofragment(fragval:Int) {
        viewPager.currentItem = fragval
    }


/*
    override fun onBackPressed() {
        if (viewPager.getCurrentItem() === 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
*/

    private fun run() {
        viewPager.adapter = ViewPagerFragmentAdapter(this)
        viewPager.setPageTransformer(GateTransformation())
        /*// attaching tab mediator
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab, position -> tab.text = titles[position] }.attach()*/
    }

    private class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        //creating fragments to show when selected or slid to
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return LoginFragment()
                1 -> return OnBoardingFragment1()
                2 -> return OnBoardingFragment2()
            }
            return LoginFragment()
        }

        override fun getItemCount(): Int {
            return 3
        }
    }
}
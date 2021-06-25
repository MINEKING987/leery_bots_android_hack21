package com.me.book_o_matic.adapters

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs


class GateTransformation : ViewPager2.PageTransformer {
    private val TAG = "GateAnimationn"
    override fun transformPage(page: View, position: Float) {
        page.translationX = -position * page.width
        if (position < -1) {    // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.alpha = 0F
        } else if (position <= 0) {    // [-1,0]
            page.alpha = 1F
            page.pivotX = 0F
            page.rotationY = 90 * abs(position)
        } else if (position <= 1) {    // (0,1]
            page.alpha = 1F
            page.pivotX = page.width.toFloat()
            page.rotationY = -90 * abs(position)
        } else {    // (1,+Infinity]
            // This page is way off-screen to the right.
            page.alpha = 0F
        }
    }
}
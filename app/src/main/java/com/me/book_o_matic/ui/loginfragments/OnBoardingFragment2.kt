package com.me.book_o_matic.ui.loginfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.me.book_o_matic.R
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout


class OnBoardingFragment2 : Fragment() {
    lateinit var mFlowLayout: TagFlowLayout
    lateinit var mInflater:LayoutInflater
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_on_boarding2,container,false)
        mFlowLayout = view.findViewById(R.id.flowLayout)
        mInflater= LayoutInflater.from(this.activity)
        val strs = arrayOf("1stbdgergfdgdrgdrg","2nddargdgdrgfta","rfseffs")
        mFlowLayout.adapter = object : TagAdapter<String?>(strs) {

            override fun getView(parent: FlowLayout?, position: Int, s: String?): View? {

                val tv:TextView = mInflater.inflate(
                    R.layout.layout_tags,
                    mFlowLayout, false
                ) as TextView
                tv.text = " $s "
                tv.background=resources.getDrawable(R.drawable.style_roundedcorners_unselected)
                return tv
            }

            override fun onSelected(position: Int, view: View?) {
                super.onSelected(position, view)
                view!!.background = resources.getDrawable(R.drawable.style_roundedcorners_selected)

            }
            override fun unSelected(position: Int, view: View?) {
                super.unSelected(position, view)
                view!!.background=resources.getDrawable(R.drawable.style_roundedcorners_unselected)
            }
        }
        return view
    }
}
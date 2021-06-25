package com.me.book_o_matic.ui.loginfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.me.book_o_matic.R
import com.me.book_o_matic.ui.LoginActivity

class OnBoardingFragment1: Fragment() {
    lateinit var button: Button
    lateinit var name:EditText
    lateinit var penName:EditText
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?
                              , savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_on_boarding1, container, false)

        name = view.findViewById(R.id.editText_name)
        penName = view.findViewById(R.id.editText_PenName)
        button = view.findViewById(R.id.button_onboard1)
        button.setOnClickListener(View.OnClickListener {
            (activity as LoginActivity).movetofragment(2)
        })
        return view
    }


}
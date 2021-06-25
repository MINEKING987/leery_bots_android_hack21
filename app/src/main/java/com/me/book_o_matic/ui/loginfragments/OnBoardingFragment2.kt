package com.me.book_o_matic.ui.loginfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.me.book_o_matic.R
import com.me.book_o_matic.firebasemodels.User
import com.me.book_o_matic.ui.MainActivity
import com.me.book_o_matic.utils.INTERESTS
import com.me.book_o_matic.utils.Repository
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout


class OnBoardingFragment2() : Fragment() {
    lateinit var mFlowLayout: TagFlowLayout
    lateinit var mInflater:LayoutInflater
    lateinit var button:Button
    val db:FirebaseFirestore = FirebaseFirestore.getInstance()
    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var repo:Repository
    var Interests:HashMap<Int,String> = HashMap()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repo = Repository(requireActivity().applicationContext)
        val view = inflater.inflate(R.layout.fragment_on_boarding2,container,false)
        mFlowLayout = view.findViewById(R.id.flowLayout)
        mInflater= LayoutInflater.from(this.activity)
        val strs = INTERESTS.split(",")
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
                Interests[position] = (view as TextView).text.toString()
            }
            override fun unSelected(position: Int, view: View?) {
                super.unSelected(position, view)
                view!!.background=resources.getDrawable(R.drawable.style_roundedcorners_unselected)
                Interests.remove(position)
            }

        }

        button = view.findViewById(R.id.button_onboard2)
        button.setOnClickListener(View.OnClickListener {
            val intrests = ArrayList(Interests.values)
            if(intrests.size < 3){
                Toast.makeText(this.activity,"Please Select 3 or more Choices",Toast.LENGTH_SHORT).show()
            }
            else{
                val interestsRef = db.collection("User").document(auth.currentUser.uid)
                val separatedinterests = intrests.joinToString { it }
                interestsRef.update("interests",separatedinterests).addOnSuccessListener {
                    val penNameref = db.collection("User").document(auth.currentUser.uid)
                    penNameref.get().addOnSuccessListener { documentSnapshot ->
                        val doc = documentSnapshot.toObject(User::class.java)
                        repo.setpenName(doc!!.username)
                        repo.setisloggedin(true)
                        repo.setuserID(auth.currentUser.uid)
                        repo.setuserIntrests(doc!!.interests)
                        val intent = Intent(this.activity,MainActivity::class.java)
                        startActivity(intent)
                    }
                    val i = Intent(this.activity,MainActivity::class.java)
                    startActivity(i)
                }
            }
        })
        return view
    }
}
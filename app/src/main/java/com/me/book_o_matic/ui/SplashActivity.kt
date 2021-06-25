package com.me.book_o_matic.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.me.book_o_matic.R
import com.me.book_o_matic.utils.Repository

class SplashActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    private var mDelayHandler: Handler? = null
    private val SPLASHDELAY: Long = 3000 //3 seconds
    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            run()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, SPLASHDELAY)


    }
    private fun run(){
        val repository = Repository(this)
        if (auth.currentUser != null) {
            if(repository.getonBoarding()){TODO("GO TO Main ACTIVITY")}
            else{
                intent = Intent(this,LoginActivity::class.java)
                intent.putExtra("goto",1)
                startActivity(intent)
            }
        } else {
            intent = Intent(this,LoginActivity::class.java)
            intent.putExtra("goto",0)
            startActivity(intent)
        }
    }
}
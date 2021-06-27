package com.me.book_o_matic.ui.loginfragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.me.book_o_matic.R
import com.me.book_o_matic.firebasemodels.User
import com.me.book_o_matic.ui.LoginActivity
import com.me.book_o_matic.ui.MainActivity
import com.me.book_o_matic.utils.Repository


class LoginFragment() : Fragment(){
    val db:FirebaseFirestore = FirebaseFirestore.getInstance()
    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    val cloudMessage = FirebaseMessaging.getInstance()
    lateinit var repo:Repository
    private lateinit var loginbutton:Button
    private val signInLauncher = registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
        this.onSignInResult(res)
    }
    private val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
    private val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login,container,false)
        repo = Repository(view.context)
        loginbutton=view.findViewById(R.id.button_login)
        loginbutton.setOnClickListener(View.OnClickListener {
            signInLauncher.launch(signInIntent)
        })

        return view
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val user = auth.currentUser
            if(user.metadata.creationTimestamp == user.metadata.lastSignInTimestamp){
                (activity as LoginActivity).movetofragment(1)
                //first time sign-in
            }
            else{

                val penNameref = db.collection("User").document(auth.currentUser.uid)
                penNameref.get().addOnSuccessListener { documentSnapshot ->
                    val doc = documentSnapshot.toObject(User::class.java)
                    repo.setpenName(doc!!.username)
                    repo.setisloggedin(true)
                    repo.setuserID(auth.currentUser.uid)
                    repo.setuserIntrests(doc!!.interests)
                    repo.setuserSubs(doc!!.subs)
                    val subarr = doc!!.subs.split(",")
                    if(subarr.isNotEmpty()){

                    }

                    val intent = Intent(this.activity,MainActivity::class.java)
                    startActivity(intent)
                }

            }
            // ...
        } else {
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.error!!.errorCode == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            showSnackbar(R.string.unknown_error);
            Log.e("LoginFragment", "Sign-in error: ", response.error);
        }
    }

    private fun showSnackbar(signInCancelled: Int) {
        Toast.makeText(this.activity,resources.getText(signInCancelled),Toast.LENGTH_SHORT).show()

    }
}

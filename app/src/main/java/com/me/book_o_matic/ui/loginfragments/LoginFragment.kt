package com.me.book_o_matic.ui.loginfragments

import android.R.attr.data
import android.app.Activity.RESULT_OK
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
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.me.book_o_matic.R
import com.me.book_o_matic.ui.LoginActivity


class LoginFragment: Fragment(){
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
        loginbutton=view.findViewById(R.id.button_login)
        loginbutton.setOnClickListener(View.OnClickListener {
            signInLauncher.launch(signInIntent)
        })
        return view
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            if(user.metadata.creationTimestamp == user.metadata.lastSignInTimestamp){
                (activity as LoginActivity).movetofragment(1)
                //first time sign-in
            }
            else{
                TODO("relogtasks() and go to main activity")
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

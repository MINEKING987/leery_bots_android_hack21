package com.me.book_o_matic.utils

import android.content.Context

class Repository(context: Context) : SharedPreferencesUtilities(context, SHARED_PREF_NAME){
    fun getonBoarding(): Boolean {
        return getBoolean(USER_ONBOARDING_DONE,false)
    }
    fun setisloggedin(bool:Boolean){
        putBoolean(USER_ONBOARDING_DONE,bool)
    }
}
package com.me.book_o_matic.utils

import android.content.Context

class Repository(context: Context) : SharedPreferencesUtilities(context, SHARED_PREF_NAME){

    val userid:String = ""

    fun setisloggedin(bool:Boolean){
        putBoolean(USER_ONBOARDING_DONE,bool)
    }
    fun setpenName(string: String){
        putString(USER_PENNAME,string)
    }
    fun setuserID(string: String){
        putString(USER_UID,string)
    }
    fun setuserIntrests(string: String){
        putString(USER_INTERESTS,string)
    }

    fun getpenName():String{
       return getString(USER_PENNAME,"")!!
    }
    fun getonBoarding(): Boolean {
        return getBoolean(USER_ONBOARDING_DONE,false)
    }
    fun getuserID():String{
       return getString(USER_UID,"")!!
    }
    fun getuserInterests():String{
        return getString(USER_INTERESTS,"")!!
    }
}
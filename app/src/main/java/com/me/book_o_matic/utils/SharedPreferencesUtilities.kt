package com.me.book_o_matic.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.lang.reflect.Type
import java.util.*

open class SharedPreferencesUtilities(context: Context, sharedPrefName: String?) {
    private val DEFAULT_INTEGER = -1
    private val DEFAULT_LONG = 0L
    private val DEFAULT_FLOAT = 0f
    private val DEFAULT_BOOLEAN = false
    private val DEFAULT_HASH_SET = HashSet<String>()

    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var gson: Gson? = null

    init {
        sharedPreferences = context.applicationContext.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        gson = Gson()
    }

    protected fun getInt(key: String?): Int? {
        return getInt(key, DEFAULT_INTEGER)
    }

    protected fun getInt(key: String?, defaultValue: Int?): Int {
        return sharedPreferences!!.getInt(key, defaultValue!!)
    }

    protected fun putString(key: String?, value: String?) {
        if (value == null) {
            removeKey(key)
        } else {
            editor!!.putString(key, value)
            editor!!.apply()
        }
    }

    protected fun getString(key: String?): String? {
        return getString(key, null)
    }

    protected fun getString(key: String?, defValue: String?): String? {
        return sharedPreferences!!.getString(key, defValue)
    }

    protected fun putLong(key: String?, value: Long?) {
        if (value == null) {
            removeKey(key)
        } else {
            editor!!.putLong(key, value)
            editor!!.apply()
        }
    }

    protected fun getLong(key: String?): Long? {
        return getLong(key, DEFAULT_LONG)
    }

    protected fun getLong(key: String?, defValue: Long?): Long? {
        return sharedPreferences!!.getLong(key, defValue!!)
    }

    protected fun putDouble(key: String?, value: Double?) {
        if (value == null) {
            removeKey(key)
        } else {
            editor!!.putLong(key, java.lang.Double.doubleToRawLongBits(value))
            editor!!.apply()
        }
    }

    protected fun getDouble(key: String?): Double? {
        return getDouble(key, null)
    }

    protected fun getDouble(key: String?, defValue: Double?): Double? {
        return java.lang.Double.longBitsToDouble(sharedPreferences!!.getLong(key, java.lang.Double.doubleToRawLongBits(defValue!!)))
    }

    protected fun putFloat(key: String?, value: Float?) {
        if (value == null) {
            removeKey(key)
        } else {
            editor!!.putFloat(key, value)
            editor!!.apply()
        }
    }

    protected fun getFloat(key: String?): Float? {
        return getFloat(key, DEFAULT_FLOAT)
    }

    protected fun getFloat(key: String?, defValue: Float?): Float? {
        return sharedPreferences!!.getFloat(key, defValue!!)
    }

    protected fun putBoolean(key: String?, value: Boolean?) {
        if (value == null) {
            removeKey(key)
        } else {
            editor!!.putBoolean(key, value)
            editor!!.apply()
        }
    }

    protected fun getBoolean(key: String?): Boolean? {
        return getBoolean(key, DEFAULT_BOOLEAN)
    }

    protected fun getBoolean(key: String?, defValue: Boolean?): Boolean {
        return sharedPreferences!!.getBoolean(key, defValue!!)
    }

    protected fun putObject(key: String?, value: Any?) {
        if (value == null) {
            removeKey(key)
        } else {
            val str = gson!!.toJson(value)
            editor!!.putString(key, str)
            editor!!.apply()
        }
    }

    protected fun <T> getObject(key: String?, tClass: Class<T>?): T {
        return getObject(key, tClass, null)!!
    }

    protected fun <T> getObject(key: String?, tClass: Class<T>?, defValue: T?): T? {
        if (contains(key)) {
            val str = getString(key)
            if (str != null) {
                return gson!!.fromJson(str, tClass)
            }
        }
        return defValue
    }

    protected fun <T> getObject(key: String?, tClass: Type?, defValue: T): T {
        if (contains(key)) {
            val str = getString(key)
            if (str != null) {
                return gson!!.fromJson(str, tClass)
            }
        }
        return defValue
    }

    protected operator fun contains(key: String?): Boolean {
        return sharedPreferences!!.contains(key)
    }

    protected fun removeKey(key: String?) {
        editor!!.remove(key)
    }

    protected fun putInt(key: String?, value: Int?) {
        if (value == null) {
            removeKey(key)
        } else {
            editor!!.putInt(key, value)
            editor!!.apply()
        }
    }

    protected fun putStringSet(key: String?, stringHashSet: HashSet<String?>?) {
        if (stringHashSet == null) {
            removeKey(key)
        } else {
            editor!!.putStringSet(key, stringHashSet)
            editor!!.apply()
        }
    }

    protected fun getStringSet(key: String?): HashSet<String?>? {
        return getStringSet(key, DEFAULT_HASH_SET)
    }

    protected fun getStringSet(key: String?, defaultValue: HashSet<String>?): HashSet<String?>? {
        return sharedPreferences!!.getStringSet(key, defaultValue) as HashSet<String?>?
    }

    protected fun clearAll() {
        editor!!.clear()
        editor!!.apply()
    }
}
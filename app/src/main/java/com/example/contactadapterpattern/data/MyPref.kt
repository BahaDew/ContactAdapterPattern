package com.example.contactadapterpattern.data

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPref @Inject constructor(
    private val shrp: SharedPreferences
) {

    fun putString(key: String, value: String) {
        shrp.edit().putString(key, value).apply()
    }

    fun getString(key: String, default: String): String {
        return shrp.getString(key, default)!!
    }

    fun putBoolean(key: String, value: Boolean) {
        shrp.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, default: Boolean): Boolean {
        return shrp.getBoolean(key, default)
    }

    fun putInt(key: String, value: Int) {
        shrp.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, default: Int): Int {
        return shrp.getInt(key, default)
    }
}
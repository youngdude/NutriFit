package com.example.nutrifit.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString("auth_token", token)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("is_logged_in", false)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}

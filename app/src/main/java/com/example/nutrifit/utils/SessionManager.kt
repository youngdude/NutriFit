package com.example.nutrifit.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        prefs.edit().putString("AUTH_TOKEN", token).apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun saveUsername(username: String) {
        prefs.edit().putString("USER_NAME", username).apply()
    }

    fun getUsername(): String? {
        return prefs.getString("USER_NAME", null)
    }

    fun isLoggedIn(): Boolean {
        return prefs.contains("AUTH_TOKEN")
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}

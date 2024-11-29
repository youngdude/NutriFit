package com.example.nutrifit.retrofit.firebase

import com.google.firebase.auth.FirebaseAuth

object AuthHelper {
    private val auth = FirebaseAuth.getInstance()

    fun getIdToken(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val user = auth.currentUser
        user?.getIdToken(true)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result?.token
                if (idToken != null) {
                    onSuccess(idToken)
                } else {
                    onFailure(Exception("ID Token is null"))
                }
            } else {
                onFailure(task.exception ?: Exception("Failed to get ID Token"))
            }
        }
    }
}
package com.example.nutrifit.retrofit.firebase

import android.app.Application
import com.google.firebase.FirebaseApp

class FirebaseInitializer : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
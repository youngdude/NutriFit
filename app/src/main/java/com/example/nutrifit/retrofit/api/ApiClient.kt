package com.example.nutrifit.retrofit.api

import android.content.Context
import com.example.nutrifit.utils.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://backend-dot-nutrifit-8f962.et.r.appspot.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun getAuthInterceptor(sessionManager: SessionManager) = okhttp3.Interceptor { chain ->
        val token = sessionManager.getAuthToken()
        val request = chain.request().newBuilder()
        if (token != null) {
            request.addHeader("Authorization", "Bearer $token")
        }
        chain.proceed(request.build())
    }

    fun getApiService(context: Context): ApiService {
        val sessionManager = SessionManager(context)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(getAuthInterceptor(sessionManager))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
//    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(loggingInterceptor)
//        .build()
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
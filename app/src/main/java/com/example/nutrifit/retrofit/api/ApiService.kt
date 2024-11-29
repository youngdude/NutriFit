package com.example.nutrifit.retrofit.api

import com.example.nutrifit.retrofit.model.LoginRequest
import com.example.nutrifit.retrofit.model.LoginResponse
import com.example.nutrifit.retrofit.model.RegisterRequest
import com.example.nutrifit.retrofit.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users")
    suspend fun registerUser(@Body request: RegisterRequest): RegisterResponse

    @POST("/login")
    suspend fun loginUser(@Body request: LoginRequest): LoginResponse
}
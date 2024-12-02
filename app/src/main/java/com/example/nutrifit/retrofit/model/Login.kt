package com.example.nutrifit.retrofit.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: String,
    val message: String,
    val data: LoginData?,
)

data class LoginData(
    val userId: String?,
    val token: String?,
    val email: String?,
    val name: String?
)
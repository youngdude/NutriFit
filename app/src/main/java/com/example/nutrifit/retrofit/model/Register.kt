package com.example.nutrifit.retrofit.model


data class RegisterRequest(
    val name: String,
    val userName: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val status: String,
    val message: String,
    val data: UserData
)

data class UserData(
    val userId: String
)
package com.example.nutrifit.data

data class YourMenu(
    val category: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val isCategory: Boolean = false
)

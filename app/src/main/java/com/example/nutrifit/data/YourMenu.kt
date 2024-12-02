package com.example.nutrifit.data

sealed class YourMenu {
    data class CategoryLabel(val category: String) : YourMenu()
    data class MenuItem(
        val category: String,
        val title: String,
        val type: String,
        val imageResId: Int
    ) : YourMenu()
}
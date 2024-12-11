package com.example.nutrifit.data

data class YourMenu(
    val category: String,
    val items: List<MenuItem>
) {
    data class MenuItem(
        val title: String,
        val type: String,
        val imageUrl: String
    )
}

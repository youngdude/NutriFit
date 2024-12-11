package com.example.nutrifit.data

data class YourMenu(
    val category: String,
    val items: List<MenuItem>
) {
    data class MenuItem(
        val imageUrl: String,
        val title: String,
        val type: String,
        val calorie: String,
        val recipe: List<String>,
        val quantities: List<String>,
        val step: String?
    )
}

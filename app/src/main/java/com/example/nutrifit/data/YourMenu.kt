//package com.example.nutrifit.data
//
//sealed class YourMenu {
//    data class CategoryLabel(val category: String) : YourMenu()
//    data class MenuItem(
//        val category: String,
//        val title: String,
//        val type: String,
//        val imageResId: Int
//    ) : YourMenu()
//}

// YourMenu.kt
package com.example.nutrifit.data

data class YourMenu(
    val category: String,
    val items: List<MenuItem>
) {
    data class MenuItem(
        val name: String,
        val description: String,
        val imageRes: Int
    )
}

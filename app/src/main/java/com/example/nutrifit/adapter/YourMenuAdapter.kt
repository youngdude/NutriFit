package com.example.nutrifit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.data.YourMenu
import com.example.nutrifit.databinding.CategoryItemBinding
import com.example.nutrifit.databinding.YourMenuItemBinding

class YourMenuAdapter(private val yourMenus: List<YourMenu>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        // Determine if the current item is a category or a menu item
        return if (yourMenus[position].items.isEmpty()) {
            VIEW_TYPE_CATEGORY
        } else {
            VIEW_TYPE_ITEM
        }
    }

    class CategoryViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryLabel: YourMenu) {
            binding.tvCategoryLabel.text = categoryLabel.category
        }
    }

    class MenuItemViewHolder(private val binding: YourMenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: YourMenu.MenuItem) {
            binding.tvTitle.text = menu.name
            binding.tvType.text = menu.description
            binding.tvImage.setImageResource(menu.imageRes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CATEGORY -> {
                val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CategoryViewHolder(binding)
            }
            VIEW_TYPE_ITEM -> {
                val binding = YourMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MenuItemViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = yourMenus.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = yourMenus[position]
        when (holder) {
            is CategoryViewHolder -> holder.bind(item) // Use the YourMenu object as CategoryLabel
            is MenuItemViewHolder -> item.items.forEach { menuItem ->
                holder.bind(menuItem) // Bind MenuItems to MenuItemViewHolder
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_ITEM = 1
    }
}

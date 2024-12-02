package com.example.nutrifit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.data.YourMenu
import com.example.nutrifit.databinding.CategoryItemBinding
import com.example.nutrifit.databinding.YourMenuItemBinding

class YourMenuAdapter(private val yourMenus: List<YourMenu>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (yourMenus[position]) {
            is YourMenu.CategoryLabel -> VIEW_TYPE_CATEGORY
            is YourMenu.MenuItem -> VIEW_TYPE_ITEM
        }
    }

    class CategoryViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryLabel: YourMenu.CategoryLabel) {
            binding.tvCategoryLabel.text = categoryLabel.category
        }
    }

    class MenuItemViewHolder(private val binding: YourMenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: YourMenu.MenuItem) {
            binding.tvTitle.text = menu.title
            binding.tvType.text = menu.type
            binding.tvImage.setImageResource(menu.imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CATEGORY) {
            val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CategoryViewHolder(binding)
        } else {
            val binding = YourMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MenuItemViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = yourMenus.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = yourMenus[position]
        when (holder) {
            is CategoryViewHolder -> holder.bind(item as YourMenu.CategoryLabel)
            is MenuItemViewHolder -> holder.bind(item as YourMenu.MenuItem)
        }
    }

    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_ITEM = 1
    }
}
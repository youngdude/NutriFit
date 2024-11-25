package com.example.nutrifit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.data.YourMenu
import com.example.nutrifit.databinding.CategoryItemBinding
import com.example.nutrifit.databinding.YourMenuItemBinding

class YourMenuAdapter(private val yourMenus: List<YourMenu>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (yourMenus[position].isCategory) VIEW_TYPE_CATEGORY else VIEW_TYPE_ITEM
    }

    class CategoryViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(yourMenu: YourMenu) {
            binding.categoryYourMenu.text = yourMenu.category
        }
    }

    class MenuItemViewHolder(private val binding: YourMenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(yourMenu: YourMenu) {
            binding.titleYourMenu.text = yourMenu.title
            binding.descYourMenu.text = yourMenu.description
            binding.imageYourMenu.setImageResource(yourMenu.imageResId)
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
        if (holder is CategoryViewHolder) {
            holder.bind(item)
        } else if (holder is MenuItemViewHolder) {
            holder.bind(item)
        }
    }
}
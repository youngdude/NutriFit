package com.example.nutrifit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutrifit.data.YourMenu
import com.example.nutrifit.databinding.YourMenuItemBinding

class YourMenuAdapter(private val yourMenus: List<YourMenu>) : RecyclerView.Adapter<YourMenuAdapter.YourMenuViewHolder>() {
    class YourMenuViewHolder(private val binding: YourMenuItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(yourMenu: YourMenu) {
            binding.categoryYourMenu.text = yourMenu.category
            binding.titleYourMenu.text = yourMenu.title
            binding.descYourMenu.text = yourMenu.description
            binding.imageYourMenu.setImageResource(yourMenu.imageResId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourMenuViewHolder {
        val binding = YourMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YourMenuViewHolder(binding)
    }

    override fun getItemCount(): Int = yourMenus.size

    override fun onBindViewHolder(holder: YourMenuViewHolder, position: Int) {
        holder.bind(yourMenus[position])
    }
}
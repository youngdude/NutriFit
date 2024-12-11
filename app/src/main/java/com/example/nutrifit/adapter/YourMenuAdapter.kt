package com.example.nutrifit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nutrifit.R
import com.example.nutrifit.data.YourMenu
import com.example.nutrifit.databinding.YourMenuItemBinding

class YourMenuAdapter(private var yourMenus: List<YourMenu.MenuItem>) :
    RecyclerView.Adapter<YourMenuAdapter.YourMenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourMenuViewHolder {
        val binding =
            YourMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YourMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YourMenuViewHolder, position: Int) {
        val menu = yourMenus[position]
        holder.bind(menu)
    }

    override fun getItemCount(): Int = yourMenus.size

    class YourMenuViewHolder(private val binding: YourMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: YourMenu.MenuItem) {
            with(binding) {
                tvTitle.text = menu.title
                tvType.text = menu.type
                Glide.with(root.context)
                    .load(menu.imageUrl)
                    .placeholder(R.drawable.default_food)
                    .into(ivImage)
            }
        }
    }

    fun updateData(newMenuList: List<YourMenu.MenuItem>) {
        yourMenus = newMenuList
        notifyDataSetChanged()
    }
}

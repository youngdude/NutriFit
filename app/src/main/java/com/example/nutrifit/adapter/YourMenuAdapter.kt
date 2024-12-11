package com.example.nutrifit.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nutrifit.R
import com.example.nutrifit.data.YourMenu
import com.example.nutrifit.databinding.YourMenuItemBinding
import com.example.nutrifit.ui.detailMenu.DetailMenuActivity

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

    inner class YourMenuViewHolder(private val binding: YourMenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val item = yourMenus[adapterPosition]
                val context = itemView.context
                val intent = Intent(context, DetailMenuActivity::class.java).apply {
                    putExtra("image", item.imageUrl)
                    putExtra("nama_makanan", item.title)
                    putExtra("kalori", item.calorie)

                    // Menambahkan bahan dan jumlah bahan
                    val recipes = item.recipe.zip(item.quantities) { recipe, quantity ->
                        "- $recipe ($quantity)"
                    }.joinToString("\n\n") ?: "Recipes not available"
                    putExtra("bahan", recipes)

                    // Memastikan langkah memasak tidak null
                    val steps = item.step
                        ?.replace(Regex("(\\d+\\.)"), "\n$1 ")
                        ?.replace("\n", "\n\n")
                        ?.trim() ?: "Steps not available"
                    putExtra("langkah", steps)

                }
                context.startActivity(intent)
            }
        }
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

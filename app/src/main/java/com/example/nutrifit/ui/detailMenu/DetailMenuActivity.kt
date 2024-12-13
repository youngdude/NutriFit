package com.example.nutrifit.ui.detailMenu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nutrifit.databinding.ActivityDetailMenuBinding

class DetailMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrl = intent.getStringExtra("image")
        val title = intent.getStringExtra("nama_makanan")
        val calorie = intent.getStringExtra("kalori")
        val recipe = intent.getStringExtra("bahan")
        val step = intent.getStringExtra("langkah")

        binding.tvTitle.text = title
        binding.tvCalorie.text = "$calorie kcal/Serving"
        binding.tvRecipe.text = recipe
        binding.tvStep.text = step
        Glide.with(this)
            .load(imageUrl)
            .into(binding.ivPhoto)

        binding.btnBack.setOnClickListener {
            finish()
        }

    }
}
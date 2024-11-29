package com.example.nutrifit.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.databinding.ActivityLoginBinding
import com.example.nutrifit.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerPage.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
package com.example.nutrifit.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nutrifit.MainActivity
import com.example.nutrifit.databinding.ActivityLoginBinding
import com.example.nutrifit.retrofit.api.ApiClient
import com.example.nutrifit.retrofit.model.LoginRequest
import com.example.nutrifit.ui.home.HomeFragment
import com.example.nutrifit.ui.register.RegisterActivity
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fieldEmail.addTextChangedListener(emailTextWatcher)
        binding.fieldPassword.addTextChangedListener(passwordTextWatcher)

        binding.btnLogin.setOnClickListener {
            val email = binding.fieldEmail.text.toString().trim()
            val password = binding.fieldPassword.text.toString().trim()

            loginUser(email, password)
        }

        binding.registerPage.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private val emailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val email = s.toString().trim()
            if (email.isEmpty()) {
                binding.fieldEmail.error = "Email is required"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.fieldEmail.error = "Invalid email"
            } else {
                binding.fieldEmail.error = null
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val password = s.toString().trim()
            if (password.isEmpty()) {
                binding.fieldPassword.error = "Password is required"
            } else if (password.length < 8) {
                binding.fieldPassword.error = "Password must be at least 8 characters"
            } else {
                binding.fieldPassword.error = null
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.loginUser(loginRequest)
                if (response.status == "success") {
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Failed: ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                Toast.makeText(
                    this@LoginActivity,
                    "HTTP Error: $errorResponse",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
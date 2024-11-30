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

        binding.btnLogin.setOnClickListener {
            val email = binding.fieldEmail.text.toString().trim()
            val password = binding.fieldPassword.text.toString().trim()

            if (isValidInput(email, password)) {
                loginUser(email, password)
            }
        }

        binding.registerPage.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun isValidInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.fieldEmail.error = "Valid email is required"
            isValid = false
        }
        if (password.isEmpty() || password.length < 8) {
            binding.fieldPassword.error = "Password must be at least 8 characters"
            isValid = false
        }
        return isValid
    }


    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.btnLogin.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.loginUser(loginRequest)
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnLogin.isEnabled = true
                if (response.status == "success") {
                    saveSession(response.data.userId, response.data.token)
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnLogin.isEnabled = true
                val errorResponse = e.response()?.errorBody()?.string()
                Toast.makeText(
                    this@LoginActivity,
                    "HTTP Error: $errorResponse",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnLogin.isEnabled = true
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun saveSession(userId: String, token: String) {
        val sharedPref = getSharedPreferences("user_session", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putString("userId", userId)
        editor.putString("token", token)
        editor.apply()
    }
}
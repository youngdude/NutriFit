package com.example.nutrifit.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.nutrifit.databinding.ActivityRegisterBinding
import com.example.nutrifit.retrofit.api.ApiClient
import com.example.nutrifit.retrofit.model.RegisterRequest
import com.example.nutrifit.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            val name = binding.fieldName.text.toString().trim()
            val username = binding.fieldUsername.text.toString().trim()
            val email = binding.fieldEmail.text.toString().trim()
            val password = binding.fieldPassword.text.toString().trim()
            val cpassword = binding.fieldCpassword.text.toString().trim()

            if (isValidInput(name, username, email, password, cpassword)) {
                registerUser(name, username, email, password)
            }
        }

        binding.loginPage.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun isValidInput(name: String, username: String, email: String, password: String, cpassword: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.fieldName.error = "Name is required"
            isValid = false
        }
        if (username.isEmpty()) {
            binding.fieldUsername.error = "Username is required"
            isValid = false
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.fieldEmail.error = "Valid email is required"
            isValid = false
        }
        if (password.isEmpty() || password.length < 8) {
            binding.fieldPassword.error = "Password must be at least 8 characters"
            isValid = false
        }
        if (cpassword.isEmpty() || cpassword != password) {
            binding.fieldCpassword.error = "Passwords do not match"
            isValid = false
        }
        return isValid
    }

    private fun registerUser(name: String, username: String, email: String, password: String) {
        val registerRequest = RegisterRequest(name, username, email, password)

        binding.progressBar.visibility = android.view.View.VISIBLE
        binding.btnRegister.isEnabled = false

        val apiService = ApiClient.getApiService(this)

        lifecycleScope.launch {
            try {
                val response = apiService.registerUser(registerRequest)
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnRegister.isEnabled = true
                if (response.status == "success") {
                    val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                    sharedPreferences.edit()
                        .putString("USER_NAME", username)
                        .apply()

                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration successful",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Failed: ${response.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: HttpException) {
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnRegister.isEnabled = true
                val errorResponse = e.response()?.errorBody()?.string()
                Toast.makeText(
                    this@RegisterActivity,
                    "HTTP Error: $errorResponse",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnRegister.isEnabled = true
                Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
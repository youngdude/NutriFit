package com.example.nutrifit.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        binding.fieldEmail.addTextChangedListener(emailTextWatcher)
        binding.fieldPassword.addTextChangedListener(passwordTextWatcher)
        binding.fieldCpassword.addTextChangedListener(confirmPasswordTextWatcher)

        binding.btnRegister.setOnClickListener {
            val name = binding.fieldName.text.toString().trim()
            val username = binding.fieldUsername.text.toString().trim()
            val email = binding.fieldEmail.text.toString().trim()
            val password = binding.fieldPassword.text.toString().trim()

            registerUser(name, username, email, password)
        }

        binding.loginPage.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
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

    private val confirmPasswordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val password = binding.fieldPassword.text.toString().trim()
            val cpassword = s.toString().trim()

            if (cpassword.isEmpty()) {
                binding.fieldCpassword.error = "Please confirm your password"
            } else if (password != cpassword) {
                binding.fieldCpassword.error = "Password do not match"
            } else {
                binding.fieldCpassword.error = null
            }
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun registerUser(name: String, username: String, email: String, password: String) {
        val registerRequest = RegisterRequest(name, username, email, password)

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.registerUser(registerRequest)
                if (response.status == "success") {
                    Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
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
                val errorResponse = e.response()?.errorBody()?.string()
                Toast.makeText(
                    this@RegisterActivity,
                    "HTTP Error: $errorResponse",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}
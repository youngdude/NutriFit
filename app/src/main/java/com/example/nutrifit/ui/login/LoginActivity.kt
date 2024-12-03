package com.example.nutrifit.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.nutrifit.MainActivity
import com.example.nutrifit.databinding.ActivityLoginBinding
import com.example.nutrifit.retrofit.api.ApiClient
import com.example.nutrifit.retrofit.model.LoginRequest
import com.example.nutrifit.ui.register.RegisterActivity
import com.example.nutrifit.utils.SessionManager
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            navigateToMain()
            return
        }

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

        val apiService = ApiClient.getApiService(this)

        lifecycleScope.launch {
            try {
                val response = apiService.loginUser(loginRequest)
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnLogin.isEnabled = true
                if (response.status == "success" && response.data != null) {
                    sessionManager.clearSession()
                    saveSession(response.data.token ?: "")
                    sessionManager.saveUsername(response.data?.name ?: "")

                    Toast.makeText(this@LoginActivity, "Login Success, Welcome ${response.data.name}", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: HttpException) {
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnLogin.isEnabled = true
                val errorResponse = e.response()?.errorBody()?.string()
                Toast.makeText(this@LoginActivity, "HTTP Error: $errorResponse", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                binding.progressBar.visibility = android.view.View.GONE
                binding.btnLogin.isEnabled = true
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveSession(token: String) {
        sessionManager.saveAuthToken(token)
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
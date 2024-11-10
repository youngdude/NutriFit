package com.example.nutrifit.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R
import com.example.nutrifit.databinding.ActivityLoginBinding
import com.example.nutrifit.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fieldUsername.onFocusChangeListener = this
        binding.fieldPassword.onFocusChangeListener = this

        binding.registerPage.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateUsername(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.fieldUsername.text.toString()
        if (value.isEmpty()) {
            errorMessage = getString(R.string.error_field_username)
        }

        if (errorMessage != null) {
            binding.labelUsername.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validatePassword(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.fieldPassword.text.toString()
        if (value.isEmpty()) {
            errorMessage = getString(R.string.error_field_password)
        } else if (value.length < 6) {
            errorMessage = getString(R.string.error_field_min_length)
        }

        if (errorMessage != null) {
            binding.labelPassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    override fun onClick(view: View?) {
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.field_username -> {
                    if (!hasFocus) validateUsername()
                }
                R.id.field_password -> {
                    if (!hasFocus) validatePassword()
                }
            }
        }
    }

    override fun onKey(view: View?, event: Int, keyEvent: KeyEvent?): Boolean {
        return false
    }
}
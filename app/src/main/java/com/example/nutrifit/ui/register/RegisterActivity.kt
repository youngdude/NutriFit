package com.example.nutrifit.ui.register

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.nutrifit.R
import com.example.nutrifit.databinding.ActivityRegisterBinding
import com.example.nutrifit.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fieldCpassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateConfirmPasswordRealTime()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.loginPage.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
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

    private fun validateConfirmPassword(): Boolean {
        var errorMessage: String? = null
        val password: String = binding.fieldPassword.text.toString()
        val cpassword: String = binding.fieldCpassword.text.toString()
        if (cpassword != password) {
            errorMessage = getString(R.string.error_field_cpassword)
        }

        if (errorMessage != null) {
            binding.labelCpassword.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateConfirmPasswordRealTime() {
        val password = binding.fieldPassword.text.toString()
        val cpassword = binding.fieldCpassword.text.toString()

        if (cpassword == password && cpassword.isNotEmpty()) {
            binding.labelCpassword.apply {
                setEndIconDrawable(R.drawable.ic_check_circle)
                setEndIconTintList(ColorStateList.valueOf(Color.GREEN))
                isErrorEnabled = false
            }
        } else {
            binding.labelCpassword.apply {
                setEndIconDrawable(null)
                isErrorEnabled = true
                error = getString(R.string.error_field_cpassword)
            }
        }
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
                R.id.field_cpassword -> {
                    if (!hasFocus) validateConfirmPassword()
                }
            }
        }
    }

    override fun onKey(view: View?, event: Int, keyEvent: KeyEvent?): Boolean {
        return false
    }
}
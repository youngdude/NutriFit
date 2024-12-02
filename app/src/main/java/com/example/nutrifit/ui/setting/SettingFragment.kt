package com.example.nutrifit.ui.setting

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.nutrifit.databinding.FragmentSettingBinding
import com.example.nutrifit.ui.login.LoginActivity
import com.example.nutrifit.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("UseSwitchCompatOrMaterialCode", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        val username = sharedPreferences.getString("USER_NAME", "User")
        binding.tvWelcome.text = "Welcome, $username"

        val switchTheme = binding.btnTheme
        val isDarkMode = sharedPreferences.getBoolean("DARK_MODE", false)
        switchTheme.isChecked = isDarkMode

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            toggleTheme(isChecked)
        }

        val btnLogout = binding.cardLogout
        btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }

        return binding.root
    }

    private fun toggleTheme(isDarkMode: Boolean) {
        sharedPreferences.edit().putBoolean("DARK_MODE", isDarkMode).apply()

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        Toast.makeText(requireContext(), "Theme changed", Toast.LENGTH_SHORT).show()
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        val sessionManager = SessionManager(requireContext())
        sessionManager.clearSession()

        sharedPreferences.edit().remove("USER_NAME").apply()

        FirebaseAuth.getInstance().signOut()

        val intent = Intent(requireActivity(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        Toast.makeText(requireContext(), "You have been logged out", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
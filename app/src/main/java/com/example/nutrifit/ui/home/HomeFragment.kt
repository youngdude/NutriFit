package com.example.nutrifit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nutrifit.R
import com.example.nutrifit.databinding.FragmentHomeBinding
import org.tensorflow.lite.Interpreter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // TF interpreter
    private lateinit var tfliteinterpreter: Interpreter

    override fun onResume() {
        super.onResume()

        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapterGender = ArrayAdapter(requireContext(), R.layout.dropdown_item, gender)
        binding.fieldGender.setAdapter(arrayAdapterGender)

        val activity = resources.getStringArray(R.array.level)
        val arrayAdapterLevel = ArrayAdapter(requireContext(), R.layout.dropdown_item, activity)
        binding.fieldActivity.setAdapter(arrayAdapterLevel)

        val target = resources.getStringArray(R.array.target)
        val arrayAdapterTarget = ArrayAdapter(requireContext(), R.layout.dropdown_item, target)
        binding.fieldTarget.setAdapter(arrayAdapterTarget)
    }

    // Load model
    private fun loadModel() {
        val assetManager = requireContext().assets
        val modelPath = "model.tflite"
        val model = assetManager.openFd(modelPath)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
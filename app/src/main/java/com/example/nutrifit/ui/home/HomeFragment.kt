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
import com.opencsv.CSVParser
import org.apache.commons.csv.CSVFormat
import org.tensorflow.lite.Interpreter
import java.io.InputStreamReader
import java.nio.ByteBuffer

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

    private fun loadModel() {
        try {
            val assetManager = requireContext().assets
            val modelPath = "model.tflite"

            // Membaca model dari assets dan mengubahnya menjadi ByteBuffer
            val fileDescriptor = assetManager.openFd(modelPath)
            val inputStream = fileDescriptor.createInputStream()
            val channel = inputStream.channel
            val size = channel.size()
            val buffer = ByteBuffer.allocateDirect(size.toInt())
            channel.read(buffer)
            buffer.rewind() // Memastikan buffer berada di awal

            // Menggunakan ByteBuffer untuk membuat Interpreter
            tfliteinterpreter = Interpreter(buffer)
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getRecipesFromCsv(): List<String> {
        val csvInputStream = requireContext().assets.open("resep.csv")
        val csvParser = org.apache.commons.csv.CSVParser(
            InputStreamReader(csvInputStream),
            CSVFormat.DEFAULT.withHeader()
        )
        return csvParser.map { it.get("recipe_name") }
    }

    private fun processInputAndNavigate() {
        // Ambil input dari pengguna
        val weight = binding.fieldWeight.text.toString().toFloatOrNull() ?: 0f
        val height = binding.fieldHeight.text.toString().toFloatOrNull() ?: 0f
        val age = binding.fieldAge.text.toString().toIntOrNull() ?: 0
        val gender = if (binding.fieldGender.text.toString() == "Laki-laki") 1 else 0
        val activityLevel = binding.fieldActivity.text.toString().toFloatOrNull() ?: 1f
        val target = binding.fieldTarget.text.toString().toFloatOrNull() ?: 0f

        // Format input untuk model
        val inputArray = arrayOf(floatArrayOf(weight, height, age.toFloat(), gender.toFloat(), activityLevel, target))
        val output = Array(1) { FloatArray(3) } // Output berupa indeks untuk sarapan, makan siang, makan malam

        // Jalankan model
        tfliteinterpreter.run(inputArray, output)
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
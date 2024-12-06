package com.example.nutrifit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nutrifit.R
import com.example.nutrifit.databinding.FragmentHomeBinding
import org.apache.commons.csv.CSVFormat
import org.tensorflow.lite.Interpreter
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var tfliteInterpreter: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        setupDropdownMenus()
        setupButtonClickListener()
    }

    private fun setupDropdownMenus() {
        val gender = resources.getStringArray(R.array.gender)
        binding.fieldGender.setAdapter(
            ArrayAdapter(requireContext(), R.layout.dropdown_item, gender)
        )

        val activity = resources.getStringArray(R.array.level)
        binding.fieldActivity.setAdapter(
            ArrayAdapter(requireContext(), R.layout.dropdown_item, activity)
        )

        val target = resources.getStringArray(R.array.target)
        binding.fieldTarget.setAdapter(
            ArrayAdapter(requireContext(), R.layout.dropdown_item, target)
        )
    }

    private fun setupButtonClickListener() {
        binding.btnSearch.setOnClickListener {
            processInputAndShowResults()
        }
    }

    private fun loadModel() {
        try {
            val assetManager = requireContext().assets
            val modelPath = "model.tflite"
            val fileDescriptor = assetManager.openFd(modelPath)
            val inputStream = fileDescriptor.createInputStream()
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength

            val buffer = fileChannel.map(
                FileChannel.MapMode.READ_ONLY, startOffset, declaredLength
            )
            tfliteInterpreter = Interpreter(buffer)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error loading model!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRecipesFromCsv(): List<String> {
        return try {
            val csvInputStream = requireContext().assets.open("resep.csv")
            val csvParser = org.apache.commons.csv.CSVParser(
                InputStreamReader(csvInputStream),
                CSVFormat.DEFAULT.withHeader()
            )
            csvParser.map { it.get("recipe_name") }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error reading CSV file!", Toast.LENGTH_SHORT).show()
            emptyList()
        }
    }

    private fun processInputAndShowResults() {
        // Ambil input dari pengguna
        val weight = binding.fieldWeight.text.toString().toFloatOrNull()
        val height = binding.fieldHeight.text.toString().toFloatOrNull()
        val age = binding.fieldAge.text.toString().toIntOrNull()

        // Gender Mapping (Male = 1, Female = 0)
        val gender = when (binding.fieldGender.text.toString()) {
            "Male" -> 1f
            "Female" -> 0f
            else -> null
        }

        // Activity Level Mapping (InActive = 1, Lightly Active = 2, etc.)
        val activityLevel = when (binding.fieldActivity.text.toString()) {
            "InActive" -> 1f
            "Lightly Active" -> 2f
            "Moderately Active" -> 3f
            "Very Active" -> 4f
            "Extra Active" -> 5f
            else -> null
        }

        // Target Mapping (1 Kg = 1f, 2 Kg = 2f, etc.)
        val target = binding.fieldTarget.text.toString().removeSuffix(" Kg").toFloatOrNull()

        // Validasi input
        if (weight == null || height == null || age == null || gender == null || activityLevel == null || target == null) {
            Toast.makeText(requireContext(), "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
            return
        }

        // Format input untuk model (Pastikan input berukuran [1, 6] - 6 fitur)
        val inputArray = arrayOf(floatArrayOf(weight, height, age.toFloat(), gender, activityLevel, target))

        // Output array dengan shape [1, 3] untuk 3 rekomendasi
        val output = Array(1) { FloatArray(3) } // Output untuk 3 rekomendasi

        // Jalankan model
        try {
            tfliteInterpreter.run(inputArray, output)

            // Ambil resep berdasarkan skor yang dihasilkan dari model
            val recommendedRecipes = getRecipesFromCsv()
            val results = output[0].mapIndexed { index, score ->
                // Ensure index is within bounds
                val recipeName = recommendedRecipes.getOrNull(index) ?: "Unknown"
                "Recipe ${index + 1}: $recipeName (Score: $score)"
            }

            // Tampilkan hasil rekomendasi
            Toast.makeText(requireContext(), results.joinToString("\n"), Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error processing input!", Toast.LENGTH_SHORT).show()
        }
    }

}

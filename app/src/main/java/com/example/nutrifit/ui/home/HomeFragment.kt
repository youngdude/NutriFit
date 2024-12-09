package com.example.nutrifit.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nutrifit.R
import com.example.nutrifit.databinding.FragmentHomeBinding
import com.example.nutrifit.utils.TFLiteHelper
import org.apache.commons.csv.CSVFormat
import org.tensorflow.lite.Interpreter
import java.io.InputStreamReader
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
        TFLiteHelper.closeInterpreter()
    }

    override fun onResume() {
        super.onResume()
        setupDropdownMenus()
        setupButtonClickListener()
        testCsvReading()
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

    private fun getRecipesFromCsv(): List<Map<String, String>> {
        return try {
            val csvInputStream = requireContext().assets.open("resep.csv")
            val csvParser = org.apache.commons.csv.CSVParser(
                InputStreamReader(csvInputStream),
                CSVFormat.DEFAULT.withHeader()
            )

            csvParser.map { record ->
                mapOf(
                    "nama_makanan" to record.get("nama_makanan"),
                    "kalori" to record.get("kalori"),
                    "jenis" to record.get("jenis"),
                    "image" to record.get("image")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error reading CSV file!", Toast.LENGTH_SHORT).show()
            emptyList()
        }
    }

    private fun processInputAndShowResults() {
        val weight = binding.fieldWeight.text.toString().toFloatOrNull()
        val height = binding.fieldHeight.text.toString().toFloatOrNull()
        val age = binding.fieldAge.text.toString().toIntOrNull()

        val gender = when (binding.fieldGender.text.toString()) {
            "Male" -> 1f
            "Female" -> 0f
            else -> null
        }

        val activityLevel = when (binding.fieldActivity.text.toString()) {
            "InActive" -> 1f
            "Lightly Active" -> 2f
            "Moderately Active" -> 3f
            "Very Active" -> 4f
            "Extra Active" -> 5f
            else -> null
        }

        val target = binding.fieldTarget.text.toString().removeSuffix(" Kg").toFloatOrNull()

        if (weight == null || height == null || age == null || gender == null || activityLevel == null || target == null) {
            Toast.makeText(requireContext(), "Please fill in all fields correctly.", Toast.LENGTH_SHORT).show()
            return
        }

        val inputArray = arrayOf(floatArrayOf(weight, height, age.toFloat(), gender, activityLevel, target))
        val output = TFLiteHelper.runInference(
            inputArray,
            TFLiteHelper.loadModel(requireContext(), "model.tflite")
        )
        if (output != null) {
            val recommendedRecipes = getRecipesFromCsv()
            if (recommendedRecipes.size < output[0].size) {
                Toast.makeText(requireContext(), "Not enough recipes to match the model's output.", Toast.LENGTH_SHORT).show()
                return
            }

            val results = output[0].mapIndexed { index, score ->
                val recipe = recommendedRecipes.getOrNull(index)
                Pair(score, recipe)
            }.sortedByDescending { it.first }

            results.forEachIndexed { index, (score, recipe) ->
                val message = "Recipe ${index + 1}: Name=${recipe?.get("nama_makanan") ?: "Unknown"}, " +
                        "Calories=${recipe?.get("kalori") ?: "Unknown"}, " +
                        "Type=${recipe?.get("jenis") ?: "Unknown"}, " +
                        "Image=${recipe?.get("image") ?: "No Image"} (Score: $score)"
                Log.d("OUTPUT_GROUPED", message)
            }
        }
    }


    private fun testCsvReading() {
        val recipes = getRecipesFromCsv()

        if (recipes.isEmpty()) {
            Log.e("CSV_TEST", "No data found in the CSV file.")
            return
        }

        recipes.forEachIndexed { index, recipe ->
            val message = "Recipe ${index + 1}: Name=${recipe["nama_makanan"]}, " +
                    "Calories=${recipe["kalori"]}, Type=${recipe["jenis"]}, " +
                    "Image=${recipe["image"]}"
            Log.d("CSV_TEST", message)
        }
    }

}

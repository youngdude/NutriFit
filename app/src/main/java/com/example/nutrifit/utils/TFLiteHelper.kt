package com.example.nutrifit.utils

import android.content.Context
import android.widget.Toast
import org.tensorflow.lite.Interpreter
import java.nio.channels.FileChannel

object TFLiteHelper {

    private var interpreter: Interpreter? = null

    fun loadModel(context: Context, modelPath: String): Interpreter? {
        return try {
            val assetManager = context.assets
            val fileDescriptor = assetManager.openFd(modelPath)
            val inputStream = fileDescriptor.createInputStream()
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength

            val buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            interpreter = Interpreter(buffer)
            interpreter
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error loading model!", Toast.LENGTH_SHORT).show()
            null
        }
    }

    fun runInference(input: Array<FloatArray>, interpreter: Interpreter?): Array<FloatArray>? {
        return try {
            val output = Array(1) { FloatArray(5) }
            interpreter?.run(input, output)

            output
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun randomizeInput(input: Array<FloatArray>): Array<FloatArray> {
        return input.map { floatArray ->
            floatArray.map { value ->
                value + (Math.random() * 0.1 - 0.05).toFloat()
            }.toFloatArray()
        }.toTypedArray()
    }

    private fun randomizeOutput(output: Array<FloatArray>) {
        output[0].forEachIndexed { index, value ->
            output[0][index] = value + (Math.random() * 0.5 - 0.25).toFloat()
        }
    }

    fun closeInterpreter() {
        interpreter?.close()
    }
}

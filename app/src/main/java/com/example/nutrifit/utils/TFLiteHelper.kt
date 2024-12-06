package com.example.nutrifit.utils

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class TFLiteHelper(private val context: Context) {

    private var interpreter: Interpreter? = null

    fun init() {
        try {
            // Muat model dari asset dan ubah ke MappedByteBuffer
            val mappedByteBuffer = loadModelFile(context)
            interpreter = Interpreter(mappedByteBuffer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context): ByteBuffer {
        // Muat file model dari folder 'assets'
        val assetFileDescriptor = context.assets.openFd("model.tflite")
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }


    fun classify(inputData: FloatArray): Int? {
        if (interpreter == null) {
            throw IllegalStateException("Interpreter is not initialized.")
        }

        // Input: 6 features (Weight, Height, Age, Gender, Activity, Target)
        val inputBuffer = ByteBuffer.allocateDirect(6 * 4).order(ByteOrder.nativeOrder())
        inputData.forEach { inputBuffer.putFloat(it) }

        // Output: Probability for each cluster (e.g., 5 clusters -> float[5])
        val outputBuffer = ByteBuffer.allocateDirect(5 * 4).order(ByteOrder.nativeOrder())
        interpreter?.run(inputBuffer, outputBuffer)

        // Read output probabilities and return the cluster ID with max probability
        outputBuffer.rewind()
        val probabilities = FloatArray(5)
        outputBuffer.asFloatBuffer().get(probabilities)

        return probabilities.indices.maxByOrNull { probabilities[it] }
    }

    fun close() {
        interpreter?.close()
    }
}

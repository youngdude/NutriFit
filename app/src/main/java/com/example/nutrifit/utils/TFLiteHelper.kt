package com.example.nutrifit.utils

import android.app.Activity
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.TensorOperator
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.math.min

class TFLiteHelper(private val context: Activity) {

    private var imageSizeX = 0
    private var imageSizeY = 0

    private var labels: List<String>? = null
    private var tfLite: Interpreter? = null

    private var inputImageBuffer: TensorImage? = null
    private var outputProbabilityBuffer: TensorBuffer? = null
    private var probabilityProcessor: TensorProcessor? = null

    private val imageMean = 0.0f
    private val imageStd = 1.0f

    private val probabilityMean = 0.0f
    private val probabilityStd = 255.0f

    fun init() {
        try {
            val opt = Interpreter.Options()
            tfLite = Interpreter(loadModelFile(context)!!, opt)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadImage(bitmap: Bitmap): TensorImage {
        inputImageBuffer!!.load(bitmap)

        val cropSize = min(bitmap.width, bitmap.height)
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(cropSize, cropSize))
            .add(ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(getPreprocessNormalizeOp())
            .build()
        return imageProcessor.process(inputImageBuffer)
    }

    @Throws(IOException::class)
    private fun loadModelFile(activity: Activity?): MappedByteBuffer? {
        val modelName = "model.tflite"
        val fileDescriptor = activity!!.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun classifyImage(bitmap: Bitmap) {
        val imageTensorIndex = 0
        val imageShape = tfLite!!.getInputTensor(imageTensorIndex).shape()

        imageSizeY = imageShape[1]
        imageSizeX = imageShape[2]

        val imageDataType = tfLite!!.getInputTensor(imageTensorIndex).dataType()
        val probabilityTensorIndex = 0
        val probabilityShape = tfLite!!.getOutputTensor(probabilityTensorIndex).shape()
        val probabilityDataType = tfLite!!.getOutputTensor(probabilityTensorIndex).dataType()

        inputImageBuffer = TensorImage(imageDataType)
        outputProbabilityBuffer =
            TensorBuffer.createFixedSize(probabilityShape, probabilityDataType)
        probabilityProcessor = TensorProcessor.Builder().add(getPostProcessNormalizeOp()).build()
        inputImageBuffer = loadImage(bitmap)
        tfLite!!.run(inputImageBuffer!!.buffer, outputProbabilityBuffer!!.buffer.rewind())
    }

    fun showResult(): List<String>? {
        labels = try {
            FileUtil.loadLabels(context, "resep.csv")
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        val labeledProbability = TensorLabel(
            labels!!, probabilityProcessor!!.process(outputProbabilityBuffer)
        )
            .mapWithFloatValue
        val maxValueInMap = Collections.max(labeledProbability.values)
        val result: MutableList<String> = ArrayList()
        for ((key, value) in labeledProbability) {
            if (value == maxValueInMap) {
                result.add(key)
            }
        }
        return result
    }

    private fun getPreprocessNormalizeOp(): TensorOperator {
        return NormalizeOp(imageMean, imageStd)
    }

    private fun getPostProcessNormalizeOp(): TensorOperator {
        return NormalizeOp(probabilityMean, probabilityStd)
    }
}
package com.example.customcamera

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

class ImageAnalyser:ImageAnalysis.Analyzer {
    private var lastAnalysedTimeStamp = 0L
    override fun analyze(image: ImageProxy?, rotationDegrees: Int) {

        val currentTimeStamp = System.currentTimeMillis()
        if(currentTimeStamp - lastAnalysedTimeStamp >= TimeUnit.SECONDS.toMillis(1)){
            val buffer = image?.planes?.get(0)?.buffer
            //extract image data from callback
            val data = buffer?.toByteArray()

            //converts the array data into pixels
            val pixels = data?.map {
                it.toInt() and 0xFF
            }
            val luma = pixels?.average()
            lastAnalysedTimeStamp = currentTimeStamp
        }


    }
    private fun ByteBuffer.toByteArray():ByteArray{
        rewind()
        val data = ByteArray(remaining())
        get(data)
        return data
    }
}
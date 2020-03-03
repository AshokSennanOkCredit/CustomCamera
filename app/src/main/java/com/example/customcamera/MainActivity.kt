package com.example.customcamera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    lateinit var imageCapture:ImageCapture
    lateinit var analyserUseCase:ImageAnalysis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(allPermissonGranted()){
            textureView.post {
//                startCamera()
            }
        }else{
            ActivityCompat.requestPermissions(this,REQUIRED_PERMISSION, REQUEST_CODE_PERMISSIONS)
        }
        textureView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            updateTransform()
        }

    }
//    private fun startCamera(){
//        val previewConfig = PreviewConfig.Builder().apply {
//            setTargetAspectRatio(Rational(1,1))
//            setTargetResolution(Size(640,640))
//        }.build()
//
//        val preview = Preview(previewConfig)
//
//        preview.setOnPreviewOutputUpdateListener {
//            val parent = textureView.parent as ViewGroup
//            parent.removeView(textureView)
//            parent.addView(textureView,0)
//
//            textureView.surfaceTexture = it.surfaceTexture
//            updateTransform()
//        }
//        captureCameraImage()
//        analyseImage()
//        CameraX.bindToLifecycle(this,preview,imageCapture,analyserUseCase)
//    }
//
//    private fun analyseImage(){
//        val analyserConfig = ImageAnalysisConfig.Builder().apply {
//            val analyzerThread = HandlerThread("Luminosity analyse").apply { start() }
//            setCallbackHandler(Handler(analyzerThread.looper))
//            setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
//        }.build()
//
//        analyserUseCase = ImageAnalysis(analyserConfig).apply {
//            analyzer = ImageAnalyser()
//        }
//
//    }


    private fun updateTransform() {
        val matrix = Matrix()
        //find center of the findviewr
        val centerX = textureView.width/2f
        val centerY = textureView.height/2f

        val rotationDegree = when(textureView?.display?.rotation){
            Surface.ROTATION_0 ->0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegree.toFloat(),centerX,centerY)

        textureView.setTransform(matrix)

    }

//    private fun captureCameraImage(){
//        val imageCaptureConfig = ImageCaptureConfig.Builder().apply {
//            setTargetAspectRatio(Rational(1,1))
//            setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
//        }.build()
//
//        imageCapture = ImageCapture(imageCaptureConfig)
//        capture_button.setOnClickListener {
//            val file = File(externalMediaDirs.first(),"${System.currentTimeMillis()}.jpeg")
//            imageCapture.takePicture(file,object:ImageCapture.OnImageSavedListener{
//                override fun onImageSaved(file: File) {
//                    val msg = "Photo Captured Successfully! ${file.absolutePath}"
//                    Toast.makeText(baseContext,msg,Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onError(
//                    useCaseError: ImageCapture.UseCaseError,
//                    message: String,
//                    cause: Throwable?
//                ) {
//                    val msg = "Phot capture Failed: $message"
//
//                }
//
//            })
//        }
//
//    }

    //****************** permission part *************//

    private fun allPermissonGranted():Boolean{
        for(permission in REQUIRED_PERMISSION)
            if(ContextCompat.checkSelfPermission(this,permission)!=PackageManager.PERMISSION_GRANTED)
                return false

        return true

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissonGranted()){
//                startCamera()
            }else{
                Toast.makeText(this,"Permissions not granted",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    companion object {
        private const val REQUEST_CODE_PERMISSIONS= 881
    }
}

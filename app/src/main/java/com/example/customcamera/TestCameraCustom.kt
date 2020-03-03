package com.example.customcamera

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.PictureDrawable
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.customcamera.utils.Picture
import kotlinx.android.synthetic.main.activity_test_camera_custom.*
import android.graphics.BitmapFactory
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.customcamera.cameraTest.fragment.CameraXFragment


class TestCameraCustom : AppCompatActivity() {

    private var cameraXFrag: CameraXFragment? = null
    private val MY_PERMISSIONS_REQUEST_CAMERA = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_camera_custom)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                MY_PERMISSIONS_REQUEST_CAMERA)
        } else {
            cameraXFrag = CameraXFragment.getInstance()
            if (null == savedInstanceState) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, cameraXFrag!!)
                    .commitAllowingStateLoss()
            }
        }
    }
    fun cameraCapturedImage(picture: Picture) {
        imageView.setImageBitmap(pictureDrawable2Bitmap(picture))
    }

    private fun pictureDrawable2Bitmap(picture: Picture): Bitmap {
        val bmImg = BitmapFactory.decodeFile(picture.path)
        return bmImg
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                val i = 0
                val len = permissions.size
                if (grantResults.size > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    if (cameraXFrag == null) {
                        cameraXFrag = CameraXFragment.getInstance()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container, cameraXFrag!!)
                            .commitAllowingStateLoss()
                    }
                }
            }
        }
    }
}

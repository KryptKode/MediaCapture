package com.kra.mediacapture

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.bumptech.glide.Glide
import com.kra.mediacapture.databinding.ActivityMainBinding
import com.kra.mediacapture.resultcontracts.CaptureVideoEnhanced
import com.kra.mediacapture.resultcontracts.TakePictureEnhanced

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var videoUri: Uri
    private lateinit var imageUri: Uri

    private val captureVideo = registerForActivityResult(CaptureVideoEnhanced()) {
        Glide.with(binding.previewImage).load(videoUri).into(binding.previewImage)
    }

    private val captureImage = registerForActivityResult(TakePictureEnhanced()) {
        Glide.with(binding.previewImage).load(imageUri).into(binding.previewImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.captureImgWithUri.setOnClickListener {
            val output = getRandomOutputUri(true).also {
                imageUri = it
            }
            captureImage.launch(output)
        }

        binding.captureImgWithoutUri.setOnClickListener {
            launchCaptureImageIntent(requestCode = RC_IMAGE)
        }

        binding.captureVideoWithUri.setOnClickListener {
            val output = getRandomOutputUri(false).also {
                videoUri = it
            }
            captureVideo.launch(output)
        }

        binding.captureVideoWithoutUri.setOnClickListener {
            launchCaptureVideoIntent(requestCode = RC_VIDEO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.w(TAG, "extras: ${data?.extras?.keySet()?.map { Pair(it, data.extras?.get(it)) }}")
        Log.w(TAG, "data: ${data?.data}")
        if (requestCode == RC_IMAGE && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as? Bitmap
            if (bitmap != null) {
                Log.e(TAG, "loading from bitmap data: $bitmap")
                Glide.with(binding.previewImage).load(bitmap)
                    .into(binding.previewImage)
            } else {
                Log.e(TAG, "loading image from intent data: ${data?.data}")
                Glide.with(binding.previewImage).load(data?.data).into(binding.previewImage)
            }
        } else if (requestCode == RC_VIDEO && resultCode == RESULT_OK) {
            Log.e(TAG, "loading video from intent data: ${data?.data}")
            Glide.with(binding.previewImage).load(data?.data).into(binding.previewImage)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val TAG = "MainActivity"
        const val RC_IMAGE = 191
        const val RC_VIDEO = 190
    }
}
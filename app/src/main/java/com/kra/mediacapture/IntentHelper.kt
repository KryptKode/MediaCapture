package com.kra.mediacapture

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast

private val CAMERA_CANDIDATES = listOf(
    "com.simplemobiletools.camera.debugcamerax"
)


fun Activity.launchCaptureVideoIntent(outputUri: Uri? = null, requestCode: Int) {
    launchMediaCaptureIntent(MediaStore.ACTION_VIDEO_CAPTURE, outputUri, requestCode)
}

fun Activity.launchCaptureImageIntent(outputUri: Uri? = null, requestCode: Int) {
    launchMediaCaptureIntent(MediaStore.ACTION_IMAGE_CAPTURE, outputUri, requestCode)
}

private fun Activity.launchMediaCaptureIntent(action: String, outputUri: Uri?, requestCode: Int) {
    Intent(action).apply {
        outputUri?.let {
            putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        try {
            startActivityForResult(enhanceCameraIntent(this, "Select Camera app"), requestCode)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this@launchMediaCaptureIntent, "No camera app found", Toast.LENGTH_SHORT)
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this@launchMediaCaptureIntent, "Error occurred", Toast.LENGTH_SHORT)
                .show()
        }
    }
}

fun Context.enhanceCameraIntent(
    baseIntent: Intent,
    title: String,
): Intent {
    val cameraIntents =
        CAMERA_CANDIDATES.map { Intent(baseIntent).setPackage(it) }
            .filter { packageManager.queryIntentActivities(it, 0).isNotEmpty() }
            .toTypedArray()

    return if (cameraIntents.isEmpty()) {
        baseIntent
    } else {
        Intent
            .createChooser(baseIntent, title)
            .putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents)
    }
}
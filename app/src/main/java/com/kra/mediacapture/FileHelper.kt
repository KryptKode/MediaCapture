package com.kra.mediacapture

import android.app.Activity
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private const val TAG = "FileHelper"

fun Activity.getRandomOutputUri(isPhoto: Boolean): Uri {
    val outputFile = getRandomOutputFile(isPhoto)
    return FileProvider.getUriForFile(
        this,
        packageName,
        outputFile
    ).also {
        Log.w(TAG, "getRandomOutputUri: URI=$it")
        Log.w(TAG, "getRandomOutputUri: outputFile=$outputFile")
    }
}

private fun Activity.getRandomOutputFile(isPhoto: Boolean): File {
    val mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM) ?: File(
        filesDir,
        Environment.DIRECTORY_DCIM
    )
    if (!mediaStorageDir.exists()) {
        mediaStorageDir.mkdirs()
    }

    val fileName = getRandomMediaName(isPhoto)
    val extension = if (isPhoto) "jpg" else "mp4"
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        kotlin.io.path.createTempFile(mediaStorageDir.toPath(), fileName, extension).toFile()
    } else {
        createTempFile(fileName, extension, mediaStorageDir)
    }
}

private fun getRandomMediaName(isPhoto: Boolean): String {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    return if (isPhoto) {
        "IMG_$timestamp"
    } else {
        "VID_$timestamp"
    }
}
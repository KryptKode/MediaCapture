package com.kra.mediacapture.resultcontracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import com.kra.mediacapture.enhanceCameraIntent

open class TakePictureEnhanced : ActivityResultContract<Uri, Boolean>() {
    override fun createIntent(context: Context, input: Uri): Intent {
        return context.enhanceCameraIntent(
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, input), "Choose Camera app"
        )
    }

    final override fun getSynchronousResult(
        context: Context,
        input: Uri
    ): SynchronousResult<Boolean>? = null


    final override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        return resultCode == Activity.RESULT_OK
    }
}
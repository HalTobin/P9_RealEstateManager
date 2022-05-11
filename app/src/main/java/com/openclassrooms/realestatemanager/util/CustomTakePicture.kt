package com.openclassrooms.realestatemanager.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper

class CustomTakePicture : ActivityResultContract<Uri, Bitmap?>() {

    @CallSuper
    override fun createIntent(context: Context, input: Uri?): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, input)
    }

    override fun getSynchronousResult(context: Context, input: Uri?): SynchronousResult<Bitmap?>? {
        return null
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Bitmap? {
        return if (intent == null || resultCode != Activity.RESULT_OK)
            null
        else
            intent.getParcelableExtra("data")
    }
}
package com.example.talkey_android.data.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object Utils {
    fun uriToFile(context: Context, uri: Uri): File {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)

        // Create a temporary file
        val tempFile = File.createTempFile("temp_image", null, context.cacheDir)
        tempFile.deleteOnExit()

        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }

    fun copyImageToUri(sourceUri: Uri, destUri: Uri, contentResolver: ContentResolver) {
        try {
            val inputStream = contentResolver.openInputStream(sourceUri)
            val outputStream = contentResolver.openOutputStream(destUri)
            if (inputStream != null && outputStream != null) {
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun cropImage(uri: Uri, cropActivityResultContract: ActivityResultLauncher<Intent>) {
        val cropIntent = Intent("com.android.camera.action.CROP").apply {
            setDataAndType(uri, "image/*")
            putExtra("crop", "true")
            putExtra("aspectX", 1)
            putExtra("aspectY", 1)
            putExtra("return-data", true)
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
            putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString())
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        cropActivityResultContract.launch(cropIntent)
    }

    fun handleCropResult(result: ActivityResult): Uri? {
        return if (result.resultCode == Activity.RESULT_OK) {
            val croppedImageUri = result.data?.data

            croppedImageUri

        } else {
            null
        }
    }
}
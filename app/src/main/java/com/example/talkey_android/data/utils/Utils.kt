package com.example.talkey_android.data.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.example.talkey_android.data.constants.Constants.SP_FB_TOKEN_KEY
import com.example.talkey_android.data.constants.Constants.SP_NAME
import com.example.talkey_android.data.domain.model.messages.ListMessageModel
import com.example.talkey_android.data.domain.model.messages.MessageModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone


object Utils {
    fun uriToFile(context: Context, uri: Uri): File {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)

        // Create a temporary file
        val tempFile = File.createTempFile("temp_image", ".png", context.cacheDir)
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

    fun checkDateAndTime(lastMsgDate: String, isChat: Boolean): String {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val dateTime = LocalDateTime.parse(lastMsgDate, formatter)
            val date = dateTime.toLocalDate()
            val currentDate = LocalDate.now()

            if (date == currentDate && !isChat) {
                formatDate(lastMsgDate).substring(11, 16)
            } else {
                formatDate(lastMsgDate).substring(0, 10)
            }
        } else {
            formatDate(lastMsgDate).substring(0, 10)
        }
    }

    fun showDateOnce(baseResponse: List<MessageModel>): ListMessageModel {
        val messageList = ArrayList<MessageModel>()
        messageList.addAll(baseResponse)
        var lastDate = ""
        messageList.forEach { message ->
            var formattedDate = ""
            if (message.date != "") {
                formattedDate = checkDateAndTime(message.date, true)
                message.day = formattedDate
            }
            if (formattedDate == lastDate) {
                message.day = ""
            } else {
                lastDate = formattedDate
            }
        }
        return ListMessageModel(messageList.count(), messageList)
    }

    fun formatDate(date: String): String {
        val backendFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        backendFormat.timeZone = TimeZone.getTimeZone("GMT-2:00")
        val backendDate = backendFormat.parse(date)

        val localFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        localFormat.timeZone = TimeZone.getDefault()
        return localFormat.format(backendDate!!)
    }

    fun saveFirebaseToken(context: Context, firebaseToken: String) {
        val sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(SP_FB_TOKEN_KEY, firebaseToken)
        editor.apply()
        println(firebaseToken)
    }

    fun getFirebaseToken(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        val firebaseToken = sharedPreferences.getString(SP_FB_TOKEN_KEY, null)
        println(firebaseToken)
        return firebaseToken ?: ""
    }

}
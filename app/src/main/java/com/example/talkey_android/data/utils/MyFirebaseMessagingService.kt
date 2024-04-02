package com.example.talkey_android.data.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Utils.saveFirebaseToken(this, token)
        Log.i("OscarToken", token)
    }
}
package com.example.talkey_android.data.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.talkey_android.MainActivity
import com.example.talkey_android.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Utils.saveFirebaseToken(this, token)
        Log.i("OscarToken", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let {
            val title = it.title.orEmpty()
            val body = it.body.orEmpty()
            createNotification(title, body)
        }
    }

    private fun createNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(this, 999, intent, PendingIntent.FLAG_IMMUTABLE)

        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val channelId = getString(R.string.default_channel)
        val notificationBuilder = NotificationCompat.Builder(this, "")
            .setSmallIcon(R.drawable.logo_talkey)
            .setContentTitle(title)
            .setContentText(body)
            .setChannelId(channelId)
            .setAutoCancel(true)
            .setSound(sound)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.default_channel),
                "Nuevo mensaje",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}
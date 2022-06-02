package com.example.ipcaboleias.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FCMService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("FCMToken", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("FCMToken", "From: ${message.from}")

        // Check if message contains a data payload.
        if (message.data.isNotEmpty()) {
            Log.d("FCMToken", "Message data payload: ${message.data}")
        }

        // Check if message contains a notification payload.
        message.notification?.let {
            Log.d("FCMToken", "Message Notification Body: ${it.body}")
        }
    }
}
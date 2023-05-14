package com.example.fcmdemoproject

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


public class PushNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(rm: RemoteMessage) {
        // create notification channel
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            var channel = NotificationChannel("MyNotification","MyNotification",
                NotificationManager.IMPORTANCE_DEFAULT)
            var manager = getSystemService(NotificationManager::class.java) as NotificationManager;
            manager.createNotificationChannel(channel)
        }
        //log data
        Log.e("TAG", "onMessageReceived: " + rm.data["title"] + rm.data["description"] + rm.data["body"]+rm.data["user"])
        showNotification(rm.data["title"],rm.data["description"], rm.data["body"],rm.data["user"])

        super.onMessageReceived(rm);
    }

    private fun showNotification(title: String?, desc: String?,body: String?,user: String?) {

        val notificationIntent =
            Intent(applicationContext, MainActivity::class.java)
        //create pending intent
        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        }
        //create builder
        var builder = NotificationCompat.Builder(this,"MyNotification")
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_delete)
            .setContentText(desc)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        var manager = NotificationManagerCompat.from(this)
        manager.apply {
            notify(123,builder.build())
        }
    }
}
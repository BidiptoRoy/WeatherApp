package com.bidiptoroy.weatherapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bidiptoroy.weatherapp.R

class Notification(var context: Context) : ContextWrapper(context) {
    fun createNotificationChannel() {
        val channel_name = "channel_name"
        val channel_description = "channel_description"
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("NOTF", channel_name, importance)
            channel.description = channel_description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)!!
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotification(content: String?, title: String?) {
        val builder = NotificationCompat.Builder(applicationContext, "NOTF")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        val notificationManager = NotificationManagerCompat.from(this)
        val notificationId = 1
        notificationManager.notify(notificationId, builder.build())
    }

    init {

        // CHANNEL MUST BE CREATED IN THE CONSTRUCTOR
        createNotificationChannel()
    }
}
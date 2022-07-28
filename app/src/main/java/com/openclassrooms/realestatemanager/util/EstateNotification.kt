package com.openclassrooms.realestatemanager.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.activity.MainActivity
import java.util.*

object EstateNotification {

    // Push a notification when an estate has been saved
    fun createNotification(context: Context) {
        val channelId = "real_estate_manager_new_estate"
        val manager: NotificationManager =
            context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = manager.getNotificationChannel(channelId)
            if (channel == null) {
                channel = NotificationChannel(
                    channelId,
                    "New Estate",
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.description = ""
                channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                manager.createNotificationChannel(channel)
            }
        }
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_home)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_home))
            .setContentTitle("RealEstateManager")
            .setContentText(context.getString(R.string.add_edit_estate_save_notification))
        builder.setContentIntent(contentIntent)
        val m = NotificationManagerCompat.from(context.applicationContext)
        m.notify(Random().nextInt(), builder.build())
    }

}
package com.example.loadapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
// May need to pass notification Id as a arg here.
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, status: String) {
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.apply {
        putExtra("fileName", messageBody)
        putExtra("status", status)
    }

    val contentPendingIntent = PendingIntent.getActivity(applicationContext, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val action = NotificationCompat.Action.Builder(0,"Show Details",contentPendingIntent).build()
    val notificationBuilder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.githubRepo_notification_channel_id))
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle("Download Complete")
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(action)

    notify(NOTIFICATION_ID, notificationBuilder.build())
}
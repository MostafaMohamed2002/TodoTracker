package com.mostafadevo.todo

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title")
        val message = intent.getStringExtra("message")
        val id = intent.getIntExtra("id",0)
        val notification = NotificationCompat.Builder(context)
            .setContentTitle(title)
            .setChannelId(Utils.CHANNEL_ID)
            .setAutoCancel(true)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setPriority(Notification.PRIORITY_HIGH)
            //set ringtone for notification
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id,notification)
        Log.d("NotificationReceiver", "onReceive: $title $message $id")
        //open the app when notification clicked
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
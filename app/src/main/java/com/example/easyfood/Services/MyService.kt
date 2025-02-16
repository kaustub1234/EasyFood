package com.example.easyfood.Services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import com.example.easyfood.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.coroutines.coroutineContext

class MyService : Service() {

    companion object {
        fun enqueueWork(context: Context, intent: Intent) {
//            enqueueWork(context, MyService::class.java, 101, intent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    /*override fun onHandleWork(intent: Intent) {
        showNotification()
    }*/

    private fun showNotification() {
        createNotificationChannel() // Required for Android 8.0+

        val notification = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setContentTitle("Welcome to EasyFood App")
            .setContentText("Search for meals \uD83C\uDF74\uD83C\uDF74 and follow along the steps to create delicious food at home \uD83E\uDD24\n\uD83E\uDD24\n\uD83E\uDD24\n")
            .setSmallIcon(R.drawable.more_meal)
            .build()

        startForeground(123, notification)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("CHANNEL_ID", name, importance)

            val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /*override fun onStopCurrentWork(): Boolean {
        return super.onStopCurrentWork()
    }*/
}
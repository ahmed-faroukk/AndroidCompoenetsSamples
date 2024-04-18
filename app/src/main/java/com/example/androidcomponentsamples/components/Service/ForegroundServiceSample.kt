package com.example.androidcomponentsamples.components.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.androidcomponentsamples.R

enum class ServiceActions {
    START, STOP
}

class ForegroundServiceSample : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand() called")
        when (intent?.action) {
            ServiceActions.START.toString() -> startForegroundService()
            ServiceActions.STOP.toString() -> stopForegroundService()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate() called")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "onBind() called")
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.cat)
            .setContentTitle("Foreground Service")
            .setContentText("Android Circle")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        try {
            startForeground(FOREGROUND_NOTIFICATION_ID, notification)
        } catch (e: Exception) {
            Log.e(TAG, "Error starting foreground service: ${e.message}")
        }
    }

    private fun stopForegroundService() {
        stopSelf()
    }

    fun stopService(context : Context){
        val intent = Intent(context, ForegroundServiceSample::class.java).apply {
            action = ServiceActions.STOP.toString()
        }
        context.startService(intent)
    }

    fun startService(context : Context){
        val intent = Intent(context, ForegroundServiceSample::class.java).apply {
            action = ServiceActions.START.toString()
        }
        context.startService(intent)
    }


    companion object {
        private const val TAG = "MyForegroundService"
        private const val NOTIFICATION_CHANNEL_ID = "CATReloaded"
        private const val NOTIFICATION_CHANNEL_NAME = "running_notifications"
        private const val FOREGROUND_NOTIFICATION_ID = 1
    }
}

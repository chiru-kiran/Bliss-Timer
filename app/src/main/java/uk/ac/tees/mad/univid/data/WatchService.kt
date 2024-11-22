package uk.ac.tees.mad.univid.data

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import uk.ac.tees.mad.univid.MainActivity
import uk.ac.tees.mad.univid.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.seconds



class WatchService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var isRunning = false
    private var timeElapsed = 0L
    private var startTime = 0L

    companion object {
        const val CHANNEL_ID = "StopwatchServiceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START" -> startStopwatch()
            "PAUSE" -> pauseStopwatch()
            "RESET" -> resetStopwatch()
        }
        return START_STICKY
    }

    private fun startStopwatch() {
        if (isRunning) return

        startTime = System.currentTimeMillis() - timeElapsed
        isRunning = true
        startForeground(1, createNotification())

        serviceScope.launch {
            while (isRunning) {
                timeElapsed = System.currentTimeMillis() - startTime
                broadcastTimeElapsed(timeElapsed)
                delay(1000L)
            }
        }
    }

    private fun pauseStopwatch() {
        isRunning = false
        stopForeground(false)
    }

    private fun resetStopwatch() {
        isRunning = false
        timeElapsed = 0L
        broadcastTimeElapsed(timeElapsed)
        stopForeground(false)
    }

    private fun broadcastTimeElapsed(elapsedTime: Long) {
        val intent = Intent("StopwatchUpdate").apply {
            putExtra("ElapsedTime", elapsedTime)
        }
        sendBroadcast(intent)
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Stopwatch Running")
            .setContentText("Time elapsed: ${formatTime(timeElapsed.seconds)}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Stopwatch Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}

fun formatTime(duration: Duration): String {
    val hours = duration.inWholeHours
    val minutes = (duration.inWholeMinutes % 60)
    val seconds = (duration.inWholeSeconds % 60)
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
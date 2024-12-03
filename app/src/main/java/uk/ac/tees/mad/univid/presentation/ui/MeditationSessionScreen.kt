package uk.ac.tees.mad.univid.presentation.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.presentation.AppViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun MeditationSessionScreen(
    duration: String?,
    viewModel: AppViewModel,
    navController: NavHostController
) {
    var timeRemaining by remember { mutableStateOf(duration!!.toLong()) }
    var timerRunning by remember { mutableStateOf(false) }
    var lastTickTime by remember { mutableStateOf(0L) }
    var timerFinished by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(key1 = timerRunning) {
        if (timerRunning && !timerFinished) {
            lastTickTime = System.currentTimeMillis()
            while (timeRemaining > 0) {
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - lastTickTime
                lastTickTime = currentTime
                timeRemaining -= elapsedTime
                delay(1000L)
            }
            timerFinished = true
            timerRunning = false

            showTimerFinishedNotification(context)
        }
    }

    val hours = (timeRemaining / 1000) / 3600
    val minutes = ((timeRemaining / 1000) % 3600) / 60
    val seconds = (timeRemaining / 1000) % 60

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
                style = MaterialTheme.typography.displayMedium,
                fontSize = 60.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = {
                if (timerRunning) {
                    timerRunning = false
                } else {
                    lastTickTime = System.currentTimeMillis()
                    timerRunning = true
                    timerFinished = false
                }
            }) {
                Icon(
                    painter = if (timerRunning) {
                        painterResource(id = R.drawable.pause)
                    } else {
                        painterResource(id = R.drawable.play_buttton)
                    },
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }
            if (timerFinished){
                Text(text = "Congratulations ${durationToTime(duration!!.toLong())} completed")
            }
        }

        if (timerFinished) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(
                    onClick = {
                        timeRemaining = duration!!.toLong()
                        timerRunning = false
                        timerFinished = false
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Discard")
                }
                TextButton(
                    onClick = {
                        // TODO: Add logic to save the session data
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}

fun durationToTime(duration: Long) : String{
    val hours = duration / 3600000
    val minutes = (duration % 3600000) / 60000
    val seconds = (duration % 60000) / 1000
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

private fun showTimerFinishedNotification(context: Context) {
    val notificationId = 1
    val builder = NotificationCompat.Builder(context, "meditation_channel")
        .setSmallIcon(R.drawable.designer)
        .setContentTitle("Meditation Session Complete")
        .setContentText("Your meditation session has ended.")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(notificationId, builder.build())
}

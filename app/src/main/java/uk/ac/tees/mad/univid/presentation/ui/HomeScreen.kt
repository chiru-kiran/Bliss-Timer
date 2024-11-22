package uk.ac.tees.mad.univid.presentation.ui

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.univid.data.WatchService
import uk.ac.tees.mad.univid.presentation.AppViewModel
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeScreen(viewModel: AppViewModel, navController: NavHostController) {
    var isRunning by remember { mutableStateOf(false) }
    var timeElapsed by remember { mutableStateOf(0L) }
    var isReset by remember { mutableStateOf(true) }

    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = formatTime(timeElapsed.seconds),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = {
                isRunning = !isRunning
                if (isRunning) {
                    context.startService(Intent(context, WatchService::class.java).apply {
                        action = "START"
                    })
                } else {
                    context.stopService(Intent(context, WatchService::class.java))
                }
                if (!isRunning) {
                    isReset = false
                }
            }) {
                Text(if (isRunning) "Pause" else "Start")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                timeElapsed = 0L
                isRunning = false
                isReset = true
                context.stopService(Intent(context, WatchService::class.java))
            }) {
                Text("Reset")
            }
        }
    }
}
fun formatTime(duration: Duration): String {
    val hours = duration.inWholeHours
    val minutes = (duration.inWholeMinutes % 60)
    val seconds = (duration.inWholeSeconds % 60)
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
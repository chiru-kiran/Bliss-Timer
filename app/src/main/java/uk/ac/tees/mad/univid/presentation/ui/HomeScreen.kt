package uk.ac.tees.mad.univid.presentation.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.util.Log
import android.view.WindowManager
import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.presentation.AppViewModel
import uk.ac.tees.mad.univid.presentation.component.ApplicationNavigationItems
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeScreen(viewModel: AppViewModel, navController: NavHostController) {
    val isLoading = viewModel.isLoading
    var isRunning by remember { mutableStateOf(false) }
    var timeElapsed by remember { mutableStateOf(0L) }
    val context = LocalContext.current
    val isMoreVisible = remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val activity = context.findActivity()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning) {
                delay(1000L)
                timeElapsed += 1000L
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .navigationBarsPadding()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = formatTime(timeElapsed),
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 60.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                TextButton(onClick = {
                    isRunning = !isRunning
                }) {
                    Icon(painter = if (isRunning) {
                        painterResource(id = R.drawable.pause)} else {
                            painterResource(id = R.drawable.play_buttton)}, contentDescription = null,modifier = Modifier.size(30.dp))
                }
            }
        }
        Column(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 50.dp, end = 20.dp)) {
            Icon(imageVector = Icons.Rounded.KeyboardArrowDown, contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        isMoreVisible.value = !isMoreVisible.value
                    })
            if (isMoreVisible.value){
                Spacer(modifier = Modifier.height(10.dp))
                Icon(imageVector = Icons.Rounded.Settings, contentDescription = null,
                    Modifier
                        .size(30.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            navController.navigate(ApplicationNavigationItems.TimerSettingsScreen.route)
                        })
                Spacer(modifier = Modifier.height(10.dp))
                Icon(imageVector = Icons.Rounded.DateRange, contentDescription = "history",
                    Modifier
                        .size(30.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {

                        }
                )
            }
        }
        if (timeElapsed.seconds>= 1.seconds){
            Row(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)) {
                TextButton(onClick = { timeElapsed = 0L
                    isRunning = false },modifier = Modifier.weight(1f)) {
                    Text(text = "Discard")
                }
                TextButton(onClick = {
                    viewModel.addMeditationSession(context, timeElapsed.milliseconds.toString())
                },modifier = Modifier.weight(1f)) {
                    Text(text = "Save")
                }
            }
        }
        if (timeElapsed.seconds< 1.seconds) {
            val time = getDuration(context, "userDuration")
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 140.dp)
            ) {
                Text(text = "Your Custom Preset",modifier = Modifier.padding(start = 10.dp))
                Card(modifier = Modifier.padding(start = 10.dp).clickable {
                    navController.navigate(ApplicationNavigationItems.MeditationSessionScreen.createRoute(
                        time.toString()
                    ))
                }) {
                    Text(
                        text = time.milliseconds.toString(),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 60.dp)
            ) {
                Text(text = "Recommended Presets",modifier = Modifier.padding(start = 10.dp))
                Row {
                    Spacer(modifier = Modifier.width(10.dp))
                    Card(modifier = Modifier.clickable { navController.navigate(ApplicationNavigationItems.MeditationSessionScreen.createRoute("300000")) }) {
                        Text(text = "5 Minutes", modifier = Modifier.padding(10.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Card(modifier = Modifier.clickable { navController.navigate(ApplicationNavigationItems.MeditationSessionScreen.createRoute("600000")) }) {
                        Text(text = "10 Minutes", modifier = Modifier.padding(10.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Card(modifier = Modifier.clickable { navController.navigate(ApplicationNavigationItems.MeditationSessionScreen.createRoute("900000")) }) {
                        Text(text = "15 Minutes", modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }
        if (isLoading.value) {
            isRunning = false
            Box(modifier = Modifier.fillMaxSize().background(Color.Black).alpha(0.5f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

fun formatTime(milliseconds: Long): String {
    val hours = milliseconds / 3600000
    val minutes = (milliseconds % 3600000) / 60000
    val seconds = (milliseconds % 60000) / 1000
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun getDuration(context: Context, key: String): Long {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val milliseconds = sharedPreferences.getLong(key, 0L)
    Log.d("Time", "getDuration: ${milliseconds.milliseconds}")
    return milliseconds
}
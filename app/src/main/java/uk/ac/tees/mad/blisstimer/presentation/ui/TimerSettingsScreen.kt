package uk.ac.tees.mad.blisstimer.presentation.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.blisstimer.presentation.AppViewModel
import uk.ac.tees.mad.blisstimer.presentation.component.ApplicationNavigationItems
import uk.ac.tees.mad.blisstimer.ui.theme.poppins
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerSettingsScreen(viewModel: AppViewModel, navController: NavHostController) {
    var hours by remember { mutableStateOf("00") }
    var minutes by remember { mutableStateOf("00") }
    var seconds by remember { mutableStateOf("00") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row {
                    Icon(imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                navController.popBackStack()
                            })
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Settings", fontFamily = poppins,
                        modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "profile",
                        modifier = Modifier.align(Alignment.CenterVertically).padding(end = 10.dp).clickable {
                            navController.navigate(ApplicationNavigationItems.ProfileScreen.route)
                        }
                    )
                }
            })
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            // Timer settings content
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Add/Edit Your First Custom Session",
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Row {
                        Column {
                            Text(
                                text = "Hour",
                                fontFamily = poppins,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            TextField(
                                value = hours,
                                onValueChange = { hours = it },
                                modifier = Modifier
                                    .width(60.dp)
                                    .align(Alignment.CenterHorizontally),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true,
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Minute",
                                fontFamily = poppins,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            TextField(
                                value = minutes,
                                onValueChange = { minutes = it },
                                modifier = Modifier
                                    .width(60.dp)
                                    .align(Alignment.CenterHorizontally),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true

                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Second",
                                fontFamily = poppins,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            TextField(
                                value = seconds,
                                onValueChange = { seconds = it },
                                modifier = Modifier
                                    .width(60.dp)
                                    .align(Alignment.CenterHorizontally),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val duration = parseDuration(hours, minutes, seconds)
                            saveDuration(context, "userDuration", duration)
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save Duration")
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Add/Edit Your Second Custom Session",
                        fontFamily = poppins,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Row {
                        Column {
                            Text(
                                text = "Hour",
                                fontFamily = poppins,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            TextField(
                                value = hours,
                                onValueChange = { hours = it },
                                modifier = Modifier
                                    .width(60.dp)
                                    .align(Alignment.CenterHorizontally),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true,
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Minute",
                                fontFamily = poppins,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            TextField(
                                value = minutes,
                                onValueChange = { minutes = it },
                                modifier = Modifier
                                    .width(60.dp)
                                    .align(Alignment.CenterHorizontally),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true

                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Second",
                                fontFamily = poppins,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            TextField(
                                value = seconds,
                                onValueChange = { seconds = it },
                                modifier = Modifier
                                    .width(60.dp)
                                    .align(Alignment.CenterHorizontally),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val duration = parseDuration(hours, minutes, seconds)
                            saveDuration(context, "userDuration1", duration)
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save Duration")
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }
}

fun parseDuration(hours: String, minutes: String, seconds: String): Duration {
    val h = hours.toLongOrNull() ?: 0
    val m = minutes.toLongOrNull() ?: 0
    val s = seconds.toLongOrNull() ?: 0

    val totalMilliseconds = (h * 3600 + m * 60 + s) * 1000
    return totalMilliseconds.milliseconds
}


fun saveDuration(context: Context, key: String, duration: Duration) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putLong(key, duration.inWholeMilliseconds)
    editor.apply()
}
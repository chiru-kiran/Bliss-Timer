package uk.ac.tees.mad.blisstimer.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.blisstimer.presentation.AppViewModel
import uk.ac.tees.mad.blisstimer.ui.theme.poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationHistoryScreen(viewModel: AppViewModel, navController: NavHostController) {
    val meditationHistory = viewModel.meditationData
    Scaffold(topBar = {
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
                Text(text = "Meditation History", fontFamily = poppins,
                    modifier = Modifier.align(Alignment.CenterVertically))
            }
        })
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn {
                items(meditationHistory.value){ item->
                    val duration = changeMillisecondsToTime(item.duration.toLong())
                    historyView(time = duration, addingTime = item.currentTime)
                }
            }
        }
    }
}

@Composable
fun historyView(time: String, addingTime : String){
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Your session time was : ${time}", fontFamily = poppins,
                fontWeight = FontWeight.SemiBold)
            Text(text = "Added on ${addingTime}")
        }
    }
}

fun changeMillisecondsToTime(millis: Long): String{
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = (millis / (1000 * 60 * 60))
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
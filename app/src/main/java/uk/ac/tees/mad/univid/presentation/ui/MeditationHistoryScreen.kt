package uk.ac.tees.mad.univid.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.univid.presentation.AppViewModel
import uk.ac.tees.mad.univid.ui.theme.poppins

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
                    Text(text = item.duration)
                    Text(text = item.currentTime)
                }
            }
        }
    }
}
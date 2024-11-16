package uk.ac.tees.mad.univid.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.presentation.component.ApplicationNavigationItems
import uk.ac.tees.mad.univid.ui.theme.poppins

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(key1 = true){
        delay(2500L)
        navController.navigate(ApplicationNavigationItems.LoginScreen.route)
    }
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Image(painter = painterResource(id = R.drawable.designer), contentDescription = "app_icon",
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape))
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "BlissTimer",
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold)
        Text(text = "A perfect key to make the meditation better",
            fontFamily = poppins,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            fontSize = 12.sp)
    }
}
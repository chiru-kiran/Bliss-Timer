package uk.ac.tees.mad.univid.presentation.component

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.univid.presentation.ui.SplashScreen

sealed class ApplicationNavigationItems(val route : String){
    object SplashScreen : ApplicationNavigationItems("splash_screen")
}

@Composable
fun ApplicationNavigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ApplicationNavigationItems.SplashScreen.route){
        composable(ApplicationNavigationItems.SplashScreen.route){
            SplashScreen()
        }
    }
}
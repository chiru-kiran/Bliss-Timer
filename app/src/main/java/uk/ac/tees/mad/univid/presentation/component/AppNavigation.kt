package uk.ac.tees.mad.univid.presentation.component

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.univid.presentation.AppViewModel
import uk.ac.tees.mad.univid.presentation.ui.HomeScreen
import uk.ac.tees.mad.univid.presentation.ui.LoginScreen
import uk.ac.tees.mad.univid.presentation.ui.SignUpScreen
import uk.ac.tees.mad.univid.presentation.ui.SplashScreen
import uk.ac.tees.mad.univid.presentation.ui.TimerSettingsScreen

sealed class ApplicationNavigationItems(val route : String){
    object SplashScreen : ApplicationNavigationItems("splash_screen")
    object LoginScreen : ApplicationNavigationItems("login_screen")
    object SignUpScreen : ApplicationNavigationItems("sign_up_screen")
    object HomeScreen : ApplicationNavigationItems("home_screen")
    object TimerSettingsScreen : ApplicationNavigationItems("timer_settings_screen")
}

@Composable
fun ApplicationNavigation(){
    val navController = rememberNavController()
    val viewModel : AppViewModel = viewModel()


    NavHost(navController = navController, startDestination = ApplicationNavigationItems.SplashScreen.route){
        composable(ApplicationNavigationItems.SplashScreen.route){
            SplashScreen(navController)
        }
        composable(ApplicationNavigationItems.LoginScreen.route){
            LoginScreen(viewModel, navController)
        }
        composable(ApplicationNavigationItems.SignUpScreen.route){
            SignUpScreen(viewModel, navController)
        }
        composable(ApplicationNavigationItems.HomeScreen.route){
            HomeScreen(viewModel, navController)
        }
        composable(ApplicationNavigationItems.TimerSettingsScreen.route){
            TimerSettingsScreen(viewModel, navController)
        }
    }
}
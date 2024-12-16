package uk.ac.tees.mad.univid.presentation.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.univid.presentation.AppViewModel
import uk.ac.tees.mad.univid.presentation.ui.EditProfileScreen
import uk.ac.tees.mad.univid.presentation.ui.HomeScreen
import uk.ac.tees.mad.univid.presentation.ui.LoginScreen
import uk.ac.tees.mad.univid.presentation.ui.MeditationHistoryScreen
import uk.ac.tees.mad.univid.presentation.ui.MeditationSessionScreen
import uk.ac.tees.mad.univid.presentation.ui.ProfileScreen
import uk.ac.tees.mad.univid.presentation.ui.SignUpScreen
import uk.ac.tees.mad.univid.presentation.ui.SplashScreen
import uk.ac.tees.mad.univid.presentation.ui.TimerSettingsScreen

sealed class ApplicationNavigationItems(val route : String){
    object SplashScreen : ApplicationNavigationItems("splash_screen")
    object LoginScreen : ApplicationNavigationItems("login_screen")
    object SignUpScreen : ApplicationNavigationItems("sign_up_screen")
    object HomeScreen : ApplicationNavigationItems("home_screen")
    object TimerSettingsScreen : ApplicationNavigationItems("timer_settings_screen")
    object MeditationSessionScreen : ApplicationNavigationItems("meditation_session_screen/{duration}"){
        fun createRoute(duration: String) = "meditation_session_screen/$duration"
    }
    object ProfileScreen : ApplicationNavigationItems("profile_screen")
    object MeditationHistoryScreen : ApplicationNavigationItems("meditation_history_screen")
    object EditProfileScreen : ApplicationNavigationItems("edit_profile_screen")
}

@Composable
fun ApplicationNavigation(){
    val navController = rememberNavController()
    val viewModel : AppViewModel = viewModel()

    Surface {
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
        composable(ApplicationNavigationItems.MeditationSessionScreen.route) { backStackEntry ->
            val duration = backStackEntry.arguments?.getString("duration")
            MeditationSessionScreen(duration, viewModel, navController)
        }
        composable(ApplicationNavigationItems.ProfileScreen.route){
            ProfileScreen(viewModel, navController)
        }
        composable(ApplicationNavigationItems.MeditationHistoryScreen.route){
            MeditationHistoryScreen(viewModel, navController)
        }
        composable(ApplicationNavigationItems.EditProfileScreen.route){
            EditProfileScreen(viewModel, navController)
        }
    }
}
}
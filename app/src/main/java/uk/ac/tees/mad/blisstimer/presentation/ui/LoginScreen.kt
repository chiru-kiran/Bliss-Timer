package uk.ac.tees.mad.blisstimer.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.blisstimer.presentation.AppViewModel
import uk.ac.tees.mad.blisstimer.presentation.component.ApplicationNavigationItems
import uk.ac.tees.mad.blisstimer.ui.theme.poppins

@Composable
fun LoginScreen(viewModel: AppViewModel, navController: NavHostController) {
    val isLoggedIn = viewModel.isLoggedIn
    val isLoading = viewModel.isLoading
    val email = remember { mutableStateOf("") }
    val password = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    if (isLoggedIn.value){
        navController.navigate(ApplicationNavigationItems.HomeScreen.route)
    }
    Column(Modifier.padding(24.dp)) {
        Spacer(modifier = Modifier.height(120.dp))
        Text(
            text = "Welcome Back",
            fontFamily = poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            color = Color(0xFF247CFF)
        )
        Text(
            text = "We're excited to have you back, can't wait to see what you've been up since you last logged in.",
            fontFamily = poppins,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        )

        Spacer(modifier = Modifier.height(50.dp))
        TextField(value = email.value, onValueChange = { email.value = it }, label = {
            Text(text = "Email")
        },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                cursorColor = Color(0xFF247CFF),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = password.value, onValueChange = { password.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Password")
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                cursorColor = Color(0xFF247CFF),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = { viewModel.login(context, email.value, password.value) }, modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF247CFF))
        ) {
            if (isLoading.value) {
                CircularProgressIndicator()
            } else {
                Text(text = "Login", fontFamily = poppins, fontSize = 20.sp)
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "By loggin you agree to our Terms & Conditions and PrivacyPolicy",
            fontFamily = poppins,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(50.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "Don't have an account yet? ", fontFamily = poppins, fontSize = 13.sp)
            Text(text = "Sign Up", fontFamily = poppins, fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp, color = Color(0xFF247CFF),
                modifier = Modifier.clickable {
                    navController.navigate(ApplicationNavigationItems.SignUpScreen.route)
                })
        }
    }
}
package uk.ac.tees.mad.univid.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.presentation.AppViewModel
import uk.ac.tees.mad.univid.presentation.component.ApplicationNavigationItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(viewModel: AppViewModel, navController: NavHostController) {
    val user = viewModel.userData
    var name = user.value!!.name
    var email = user.value!!.email
    var number = user.value!!.number
    var newName by remember { mutableStateOf(name) }
    var newEmail by remember { mutableStateOf(email) }
    var newNumber by remember { mutableStateOf(number) }

    Scaffold(topBar =
    {
        TopAppBar(title = {
            Row {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    Text(text = "Profile", modifier = Modifier.align(Alignment.Center))
                }
            }
        })
    }) {
        if (user.value != null) {
            Column(
                modifier = Modifier.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                if (user.value!!.profilePhoto.isNotEmpty()) {
                    AsyncImage(
                        model = user.value!!.profilePhoto, contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(value = name, onValueChange ={ name = it},
                    label = { Text(text = "Full Name")})
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(value = number, onValueChange ={ number = it},
                    label = { Text(text = "Phone No")})
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(value = email, onValueChange ={ email = it},
                    label = { Text(text = "E-mail")})
                Button(onClick = {
                }) {
                    Text(text = "Edit Profile")
                }
            }
        }
    }
}
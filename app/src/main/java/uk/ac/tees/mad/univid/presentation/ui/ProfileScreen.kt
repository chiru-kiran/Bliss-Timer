package uk.ac.tees.mad.univid.presentation.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uk.ac.tees.mad.univid.R
import uk.ac.tees.mad.univid.presentation.AppViewModel
import uk.ac.tees.mad.univid.presentation.component.ApplicationNavigationItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: AppViewModel, navController: NavHostController) {
    val user = viewModel.userData
    Scaffold(topBar =
    {
        TopAppBar(title = {
            Row {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterStart).clickable {
                            navController.popBackStack()
                        }
                    )
                    Text(text = "Profile", modifier = Modifier.align(Alignment.Center))
                }
            }
        })
    }) {
        Log.d("Profile", "ProfileScreen: ${user.value}")
        if (user.value != null) {
            Column(
                modifier = Modifier.padding(it),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                if (user.value!!.profilePhoto.isNotEmpty()) {
                    AsyncImage(
                        model = user.value!!.profilePhoto, contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.size(200.dp).clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = user.value!!.name)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = user.value!!.email)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = user.value!!.number)
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    navController.navigate(ApplicationNavigationItems.EditProfileScreen.route)
                }) {
                    Text(text = "Edit Profile")
                }
                Spacer(modifier = Modifier.height(40.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.editing),
//                        contentDescription = null,
//                        modifier = Modifier.size(30.dp)
//                    )
//                    Spacer(modifier = Modifier.width(20.dp))
//                    Text(text = "Edit/Delete Saved Sessions")
//                    Spacer(modifier = Modifier.weight(1f))
//                    Icon(
//                        imageVector = Icons.Rounded.KeyboardArrowRight,
//                        contentDescription = null,
//                        modifier = Modifier.size(30.dp)
//                    )
                }
//                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp).clickable {
                            viewModel.logout()
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = "Log Out")
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}
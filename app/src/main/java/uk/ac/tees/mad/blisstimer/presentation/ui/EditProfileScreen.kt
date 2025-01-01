package uk.ac.tees.mad.blisstimer.presentation.ui

import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Edit
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uk.ac.tees.mad.blisstimer.R
import uk.ac.tees.mad.blisstimer.presentation.AppViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(viewModel: AppViewModel, navController: NavHostController) {
    val user = viewModel.userData
    var name = user.value!!.name
    var email = user.value!!.email
    var number = user.value!!.number
    val context = LocalContext.current
    var newName by remember { mutableStateOf(name) }
    var newEmail by remember { mutableStateOf(email) }
    var newNumber by remember { mutableStateOf(number) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val photoFile = remember {
        File.createTempFile("IMG_", ".jpg", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
    }

    val photoUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        photoFile
    )


    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            imageUri = photoUri
            viewModel.uploadProfilePhoto(context, photoUri)
        }
    }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                takePictureLauncher.launch(photoUri)
            } else {
                Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    )

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
        if (user.value != null) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Box(modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)) {
                    if (user.value!!.profilePhoto.isNotEmpty()) {
                        AsyncImage(
                            model = user.value!!.profilePhoto, contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .size(150.dp)
                                .clickable {
                                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                }
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = null,
                            modifier = Modifier
                                .size(150.dp)
                                .clickable {
                                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                }
                        )
                    }
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.align(
                        Alignment.BottomEnd).padding(16.dp).background(Color.Black, CircleShape))
                }
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(value = newName, onValueChange ={ newName = it},
                    label = { Text(text = "Full Name")})
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(value = newNumber, onValueChange ={ newNumber = it},
                    label = { Text(text = "Phone No")})
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(value = email, onValueChange ={ email = it},
                    label = { Text(text = "E-mail")})
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Note: Email cannot be changed")
                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = {
                    viewModel.updateUserData(context, newName, newNumber)
                }) {
                    Text(text = "Save Profile")
                }
            }
        }
    }
}
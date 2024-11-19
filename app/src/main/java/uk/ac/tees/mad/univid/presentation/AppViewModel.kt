package uk.ac.tees.mad.univid.presentation

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val authentication : FirebaseAuth,
    private val firestore : FirebaseFirestore
) : ViewModel() {
    val isLoggedIn = mutableStateOf(false)
    val isLoading = mutableStateOf(false)

    fun signUp(context: Context,email : String, password : String, number: String){
        authentication.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            
        }
    }

    fun login(context: Context,email : String, password : String){
        isLoggedIn.value = true
    }

    fun logout(){
        isLoggedIn.value = false
    }
}
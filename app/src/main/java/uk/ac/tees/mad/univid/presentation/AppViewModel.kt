package uk.ac.tees.mad.univid.presentation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val authentication : FirebaseAuth,
    private val firestore : FirebaseFirestore
) : ViewModel() {
    val isLoggedIn = mutableStateOf(false)
    val isLoading = mutableStateOf(false)

    init {
        isLoggedIn.value = authentication.currentUser != null
    }

    fun signUp(context: Context,email : String, password : String, number: String){
        isLoggedIn.value = true
        authentication.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            firestore.collection("user").document(it.user!!.uid).set(uk.ac.tees.mad.univid.domain.model.User(
                uid = it.user!!.uid,
                email = email,
                number = number,
                password = password
            )).addOnSuccessListener {
                isLoggedIn.value = true
                isLoading.value = false
                loadUserData()
                Toast.makeText(context, "User created successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                isLoading.value = false
                Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("TAG", "signUp: ${it.localizedMessage}")
            }
        }.addOnFailureListener {
            isLoading.value = false
            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
            Log.d("TAG", "signUp: ${it.localizedMessage}")
        }
    }

    fun login(context: Context,email : String, password : String){
        isLoading.value = true
        authentication.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            isLoggedIn.value = true
            isLoading.value = false
            Toast.makeText(context, "User logged in successfully", Toast.LENGTH_SHORT).show()
            loadUserData()
        }.addOnFailureListener {
            isLoading.value = false
            Toast.makeText(context, it.localizedMessage, Toast.LENGTH_SHORT).show()
            Log.d("TAG", "signUp: ${it.localizedMessage}")
        }
    }

    fun loadUserData(){

    }

    fun logout(){
        authentication.signOut()
        isLoggedIn.value = false
    }

}
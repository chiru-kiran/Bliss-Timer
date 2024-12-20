package uk.ac.tees.mad.univid.presentation

import android.content.Context
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.ac.tees.mad.univid.domain.model.MeditationSession
import uk.ac.tees.mad.univid.domain.model.User
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val authentication : FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val database : FirebaseStorage
) : ViewModel() {
    val isLoggedIn = mutableStateOf(false)
    val isLoading = mutableStateOf(false)

    val userData = mutableStateOf<User?>(null)
    val meditationData = mutableStateOf<List<MeditationSession>>(emptyList())

    init {
        isLoggedIn.value = authentication.currentUser != null
        if(isLoggedIn.value) {
            fetchMeditationData()
            loadUserData()
        }
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
        if (authentication.currentUser != null) {
            firestore.collection("user").document(authentication.currentUser!!.uid).get().addOnSuccessListener {
                val result = it.toObject(User::class.java)
                userData.value = result
                Log.d("TAG", "loadUserData: $result")
            }.addOnFailureListener {
                Log.d("TAG", "loadUserData: ${it.localizedMessage}")
            }
        }else{
            return
        }
    }

    fun addMeditationSession(context: Context, duration: String) {
        isLoading.value = true
        val currentTime = System.currentTimeMillis()
        val currentDateTime = java.util.Date(currentTime)
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        val formattedDateTime = formatter.format(currentDateTime)
        Log.d("Time", "addMeditationSession: $formattedDateTime")

        val userId = authentication.currentUser?.uid
        if (userId != null) {
            val sessionRef = firestore.collection("meditation_sessions")
                .document(userId)
                .collection("sessions")
                .document()

            val meditationSession = MeditationSession(duration = duration, currentTime = formattedDateTime)
            sessionRef.set(meditationSession)
                .addOnSuccessListener {
                    val documentId = sessionRef.id
                    firestore.collection("meditation_sessions").document(userId)
                        .collection("sessions").document(documentId).update(
                            "id" , documentId
                        ).addOnSuccessListener {
                            isLoading.value = false
                            Toast.makeText(context, "Meditation session added successfully", Toast.LENGTH_SHORT).show()
                            fetchMeditationData()
                        }.addOnFailureListener {
                            isLoading.value = false
                            Toast.makeText(context, "Error: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { exception ->
                    isLoading.value = false
                    Toast.makeText(context, "Error: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    fun fetchMeditationData() {
        val userId = authentication.currentUser?.uid

        if (userId.isNullOrEmpty()) {
            Log.e("fetchMeditationData", "User is not authenticated.")
            return
        }

        firestore.collection("meditation_sessions")
            .document(userId)
            .collection("sessions")
            .get()
            .addOnSuccessListener { documents ->
                val data = documents.toObjects(MeditationSession::class.java)
                meditationData.value = data
                Log.d("fetchMeditationData", "Fetched meditation sessions: $data")
            }
            .addOnFailureListener { exception ->
                Log.e("fetchMeditationData", "Error fetching meditation sessions: ${exception.localizedMessage}")
            }
    }

    fun logout(){
        authentication.signOut()
        isLoggedIn.value = false
        userData.value = null
    }

    fun uploadProfilePhoto(context: Context, photoUri: Uri) {
        val userId = authentication.currentUser?.uid
        val storageRef = database.reference.child("profile_photos").child(userId!!)
        val uploadTask = storageRef.putFile(photoUri)
        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                firestore.collection("user").document(userId).update("profilePhoto", uri.toString())
                    .addOnSuccessListener {
                        loadUserData()
                        Toast.makeText(
                            context,
                            "Profile photo updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {
                    Toast.makeText(context, "Error updating profile photo", Toast.LENGTH_SHORT)
                        .show()
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Error getting download URL", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Error uploading profile photo", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateUserData(context: Context, newName: String, newNumber: String) {
        firestore.collection("user").document(authentication.currentUser!!.uid).update(
            "name", newName,
            "number", newNumber
        ).addOnSuccessListener {
            loadUserData()
            Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Error updating profile", Toast.LENGTH_SHORT).show()
        }
    }

}
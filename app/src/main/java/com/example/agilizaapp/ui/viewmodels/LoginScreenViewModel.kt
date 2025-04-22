package com.example.agilizaapp.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Almacenar información del usuario
    val userPhotoUrl = mutableStateOf("")
    val userName = mutableStateOf("")

    fun signWithGoogleCredential(credential: AuthCredential, home: () -> Unit) = viewModelScope.launch {
        try {
            val auth = FirebaseAuth.getInstance()
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.let {
                            // Guardamos la foto y nombre del usuario
                            userPhotoUrl.value = it.photoUrl.toString()
                            userName.value = it.displayName ?: "Usuario"
                        }
                        Log.d("AgilizaApp", "Logueado con Google exitoso!")
                        home() // Ejecuta la función que cambia de pantalla
                    }
                }
                .addOnFailureListener {
                    Log.d("Macota feliz,", "fallo al loguera")
                }
        } catch (ex: Exception) {
            Log.d("Agikizaap", "Excepcion al loguerar google: ${ex.localizedMessage}")
        }
    }

    // Limpiar los datos del usuario al cerrar sesión
    fun clearUserData() {
        userPhotoUrl.value = ""
        userName.value = ""
    }
}

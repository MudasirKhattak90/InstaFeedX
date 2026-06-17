package com.example.instafeedx.viewmodel

import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instafeedx.data.AuthRepository
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.stream.JsonToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    private val repository = AuthRepository()


    private val  _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String){
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.loginWithEmail(email, password)
            _authState.value = if (result.isSuccess)
                AuthState.Success
            else
                AuthState.Error(result.exceptionOrNull()?.message ?: "Login Failed")
        }
    }

    fun register(email: String, password: String){
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.registerWithEmail(email, password)
            _authState.value = if (result.isSuccess)
                AuthState.Success
            else
                AuthState.Error(result.exceptionOrNull()?.message ?: "Register Failed")
        }
    }

    fun googleLogin(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.firebaseAuthWithGoogle(idToken)
            _authState.value = if (result.isSuccess)
                AuthState.Success
            else
                AuthState.Error(result.exceptionOrNull()?.message ?: "Google Login Failed")
        }

    }

    fun logout() {
        _authState.value = AuthState.Idle
    }
}
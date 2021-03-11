package com.abel.avengerit.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abel.avengerit.repositories.FirebaseRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SessionViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    private val userSessionLive = MutableLiveData<FirebaseUser?>()
    private val userSessionLoggedLive = MutableLiveData<FirebaseUser?>()

    fun registerUserFirebase(email: String, pass: String) {
        viewModelScope.launch {
            firebaseRepository.registerEmail(email, pass).collect {
                userSessionLive.value = it
            }
        }
    }

    fun loginUserFirebase(email: String, pass: String) {
        viewModelScope.launch {
            firebaseRepository.login(email, pass).collect {
                userSessionLoggedLive.value = it
            }
        }
    }

    fun loginUserFacebook() {
        viewModelScope.launch {
            firebaseRepository.logInFacebook().collect {
                userSessionLoggedLive.value = it
            }
        }
    }

    fun getUserRegister(): MutableLiveData<FirebaseUser?> {
        return userSessionLive
    }

    fun getUserLoggedIn(): MutableLiveData<FirebaseUser?> {
        return userSessionLoggedLive
    }
}
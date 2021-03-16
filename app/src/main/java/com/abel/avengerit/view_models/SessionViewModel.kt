package com.abel.avengerit.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abel.avengerit.model.local.SessionEntity
import com.abel.avengerit.repositories.FirebaseRepository
import com.facebook.AccessToken
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SessionViewModel(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    val resourseLive = MutableLiveData<Resourse<SessionEntity>>()

    fun registerUserFirebase(email: String?, pass: String?, passRepeat: String?) {
        viewModelScope.launch {
            firebaseRepository.registerEmail(email, pass, passRepeat).collect {
                resourseLive.value = it
            }
        }
    }

    fun loginUserFirebase(email: String?, pass: String?) {
        viewModelScope.launch {
            firebaseRepository.login(email, pass).collect {
                resourseLive.value = it
            }
        }
    }

    fun loginUserFacebook(token: AccessToken) {
        viewModelScope.launch {
            firebaseRepository.logInFacebook(token).collect {
                resourseLive.value = it
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            firebaseRepository.signOut()
        }
    }

    fun isUserInLog() {
        viewModelScope.launch {
            firebaseRepository.sessionActive().collect {
                resourseLive.value = it
            }
        }
    }

    fun getResourceLive(): MutableLiveData<Resourse<SessionEntity>> {
        return resourseLive
    }
}
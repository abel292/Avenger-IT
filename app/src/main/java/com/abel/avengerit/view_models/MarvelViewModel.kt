package com.abel.avengerit.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abel.avengerit.model.character.Result
import com.abel.avengerit.repositories.MarvelRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MarvelViewModel(private val marvelRepository: MarvelRepository) : ViewModel() {

    val characterLive = MutableLiveData<List<Result>?>()

    fun getCharacters() {
        viewModelScope.launch {
            marvelRepository.getCharacters().collect {
                characterLive.value = it
            }
        }
    }

}
package com.abel.avengerit.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abel.avengerit.model.character.Result
import com.abel.avengerit.model.event.Event
import com.abel.avengerit.repositories.MarvelRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MarvelViewModel(private val marvelRepository: MarvelRepository) : ViewModel() {

    val resourceCharacterLive = MutableLiveData<Resourse<List<Result>?>>()
    val eventsLive = MutableLiveData<Resourse<List<Event>?>>()

    fun getCharacters(offset: Int) {
        viewModelScope.launch {
            marvelRepository.getCharacters(offset.toString()).collect {
                resourceCharacterLive.value = it
            }
        }
    }

    fun getEvents(offset: Int) {
        viewModelScope.launch {
            marvelRepository.getEvents(offset.toString()).collect {
                eventsLive.value = it
            }
        }
    }
}
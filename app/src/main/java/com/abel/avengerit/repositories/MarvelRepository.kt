package com.abel.avengerit.repositories

import android.util.Log
import com.abel.avengerit.BuildConfig
import com.abel.avengerit.model.character.Result
import com.abel.avengerit.model.event.Event
import com.abel.avengerit.service.MarvelApi
import com.abel.avengerit.utils.stringToMd5
import com.abel.avengerit.view_models.Resourse
import com.abel.avengerit.view_models.Resourse.Companion.BAD
import com.abel.avengerit.view_models.Resourse.Companion.SUCCESS
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MarvelRepository(
    private val marvelApi: MarvelApi,
    private val resourse: Resourse<List<Result>?>
) {

    suspend fun getCharacters(offset: String) = flow {
        //Genero el hash por si tengo que hacer un cambio
        val hash = stringToMd5("1${BuildConfig.API_KEY_PRIVATE}${BuildConfig.API_KEY_PUBLIC}")
        val result = marvelApi.getCharacters("15", offset, BuildConfig.API_KEY_PUBLIC, hash, "1")
        when (result.body()?.code) {
            200 -> {
                resourse.resourceObject = result.body()?.data?.results
                resourse.responseAction = SUCCESS
            }
            404 -> {
                resourse.responseAction = BAD
            }
            401 -> {
                resourse.responseAction = BAD
            }
            else -> {
                resourse.responseAction = BAD
            }
        }
        resourse.loading = false
        kotlinx.coroutines.delay(2000)
        emit(resourse)
    }.catch {
        resourse.responseAction = BAD
        emit(resourse)
    }

    suspend fun getEvents() = flow {
        val resourceEvent = Resourse<List<Event>?>(null, null)
        val hash = stringToMd5("1${BuildConfig.API_KEY_PRIVATE}${BuildConfig.API_KEY_PUBLIC}")
        val result = marvelApi.getEvents(BuildConfig.API_KEY_PUBLIC, hash, "1")
        when (result.body()?.code) {
            200 -> {
                resourceEvent.resourceObject = result.body()?.data?.results
                resourceEvent.responseAction = SUCCESS
            }
            404 -> {
                resourceEvent.responseAction = BAD
            }
            401 -> {
                resourceEvent.responseAction = BAD
            }
            else -> {
                resourceEvent.responseAction = BAD
            }
        }
        resourceEvent.loading = false
        emit(resourceEvent.resourceObject)
    }.catch {
        resourse.loading = false
        Log.e(this@MarvelRepository.javaClass.name, "")
    }

}
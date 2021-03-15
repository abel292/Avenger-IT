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
        val hash = stringToMd5("$TS${BuildConfig.API_KEY_PRIVATE}${BuildConfig.API_KEY_PUBLIC}")
        val result = marvelApi.getCharacters(LIMIT, offset, BuildConfig.API_KEY_PUBLIC, hash, TS)
        when (result.body()?.code) {
            200 -> {
                resourse.resourceObject = result.body()?.data?.results
                resourse.responseAction = SUCCESS
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

    suspend fun getEvents(offset: String) = flow {
        val resourceEvent = Resourse<List<Event>?>(null, null)
        val hash = stringToMd5("$TS${BuildConfig.API_KEY_PRIVATE}${BuildConfig.API_KEY_PUBLIC}")
        val result = marvelApi.getEvents(LIMIT, offset, BuildConfig.API_KEY_PUBLIC, hash, TS)
        when (result.body()?.code) {
            200 -> {
                resourceEvent.resourceObject = result.body()?.data?.results
                resourceEvent.responseAction = SUCCESS
            }
            else -> {
                resourceEvent.responseAction = BAD
            }
        }
        resourceEvent.loading = false
        emit(resourceEvent)
    }.catch {
        resourse.loading = false
        Log.e(this@MarvelRepository.javaClass.name, "")
    }

    companion object {
        const val LIMIT = "15"
        const val TS = "1"
    }

}
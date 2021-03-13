package com.abel.avengerit.repositories

import android.util.Log
import com.abel.avengerit.BuildConfig
import com.abel.avengerit.service.MarvelApi
import com.abel.avengerit.utils.stringToMd5
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MarvelRepository(private val marvelApi: MarvelApi) {

    suspend fun getCharacters() = flow {
        val hash = stringToMd5("1${BuildConfig.API_KEY_PRIVATE}${BuildConfig.API_KEY_PUBLIC}")
        val result = marvelApi.getCharacters(BuildConfig.API_KEY_PUBLIC, hash, "1")
        when (val resultCode = result.body()?.code) {
            200 -> emit(result.body()?.data?.results)
            404 -> Log.e(this@MarvelRepository.javaClass.name, "ABEL $resultCode")
            401 -> Log.e(this@MarvelRepository.javaClass.name, "ABEL")
            else -> Log.e(this@MarvelRepository.javaClass.name, "ABEL $resultCode")
        }
    }.catch {
        Log.e(this@MarvelRepository.javaClass.name, "")
    }
}
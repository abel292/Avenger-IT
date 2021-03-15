package com.abel.avengerit.service

import com.abel.avengerit.model.character.ResponseApiCharacter
import com.abel.avengerit.model.event.ResponseEventApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {

    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("limit") limit: String,
        @Query("offset") offset: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String
    ): Response<ResponseApiCharacter>


    @GET("v1/public/events")
    suspend fun getEvents(
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String
    ): Response<ResponseEventApi>


}
package com.abel.avengerit.service

import com.abel.avengerit.model.character.ResponseApiMarvel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {

    /*@GET("/v1/public/characters")
    suspend fun getCharacters(@Path(value = "idEfector") idEfector: String): Response<ResponseApiMarvel>
*/
    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String
    ): Response<ResponseApiMarvel>


}
package com.abel.avengerit.modules

import com.abel.avengerit.repositories.FirebaseRepository
import com.abel.avengerit.repositories.MarvelRepository
import com.abel.avengerit.service.MarvelApi
import com.abel.avengerit.ui.main.MainActivity
import com.abel.avengerit.view_models.MarvelViewModel
import com.abel.avengerit.view_models.SessionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.androidx.compose.BuildConfig
import org.koin.androidx.compose.get
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val moduleApp = module {
    single { FirebaseRepository() }
    single { MarvelRepository(get()) }
    single { MainActivity() }
    single { getMarvelApi() }

}

fun getMarvelApi(): MarvelApi = Retrofit.Builder()
    .baseUrl("https://gateway.marvel.com")
    .addConverterFactory(GsonConverterFactory.create(getGson()))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()
    .create(MarvelApi::class.java)

fun getGson(): Gson = GsonBuilder()
    .setLenient()
    .create()

val moduleViewModels = module {
    viewModel { SessionViewModel(get()) }
    viewModel { MarvelViewModel(get()) }
}
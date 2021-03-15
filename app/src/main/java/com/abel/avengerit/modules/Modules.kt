package com.abel.avengerit.modules

import android.content.Context
import com.abel.avengerit.AvengerITAplication
import com.abel.avengerit.BuildConfig
import com.abel.avengerit.model.local.AppDatabase
import com.abel.avengerit.repositories.FirebaseRepository
import com.abel.avengerit.repositories.MarvelRepository
import com.abel.avengerit.service.MarvelApi
import com.abel.avengerit.ui.main.MainActivity
import com.abel.avengerit.view_models.MarvelViewModel
import com.abel.avengerit.view_models.Resourse
import com.abel.avengerit.view_models.SessionViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val moduleApp = module {
    single { FirebaseRepository(getDataBase(get()), get()) }
    single { MarvelRepository(get(), get()) }
    single { MainActivity() }
    single { getMarvelApi() }
    single { getDataBase(get()) }
    single { getAppContext() }
    single { Resourse(null, null) }
}

fun getMarvelApi(): MarvelApi = Retrofit.Builder()
    .baseUrl(BuildConfig.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(getGson()))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()
    .create(MarvelApi::class.java)

fun getGson(): Gson = GsonBuilder()
    .setLenient()
    .create()

fun getDataBase(context: Context): AppDatabase = AppDatabase.getInstance(context)

fun getAppContext(): AvengerITAplication = AvengerITAplication.appInstance

val moduleViewModels = module {
    viewModel { SessionViewModel(get()) }
    viewModel { MarvelViewModel(get()) }
}
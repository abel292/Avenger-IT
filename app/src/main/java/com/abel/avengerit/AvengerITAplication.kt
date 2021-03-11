package com.abel.avengerit

import android.app.Application
import com.abel.avengerit.modules.moduleApp
import com.abel.avengerit.modules.moduleViewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AvengerITAplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AvengerITAplication)
            modules(moduleApp, moduleViewModels)
        }
    }

}
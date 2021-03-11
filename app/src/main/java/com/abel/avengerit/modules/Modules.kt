package com.abel.avengerit.modules

import com.abel.avengerit.repositories.FirebaseRepository
import com.abel.avengerit.view_models.SessionViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.get
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val moduleApp = module {
    single { FirebaseRepository() }
}

val moduleViewModels = module {
    viewModel { SessionViewModel(get()) }
}
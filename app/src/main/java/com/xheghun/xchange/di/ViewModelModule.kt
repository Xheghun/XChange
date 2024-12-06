package com.xheghun.xchange.di

import com.xheghun.xchange.presentation.ui.screens.HomeViewmodel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun viewModelModule() = module {
    viewModel { HomeViewmodel(get()) }
}
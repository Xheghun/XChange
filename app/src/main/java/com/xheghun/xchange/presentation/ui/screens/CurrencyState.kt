package com.xheghun.xchange.presentation.ui.screens

import kotlinx.coroutines.flow.MutableStateFlow

data class CurrencyState(
    val currency: MutableStateFlow<String>,
    val amount: MutableStateFlow<String>
)
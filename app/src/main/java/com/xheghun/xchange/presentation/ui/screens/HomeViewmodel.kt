package com.xheghun.xchange.presentation.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xheghun.xchange.data.model.ExchangeResult
import com.xheghun.xchange.data.repo.ExchangeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewmodel(private val exchangeRepo: ExchangeRepository) : ViewModel() {
    private val _exchangeResult = MutableStateFlow<ExchangeResult?>(null)
    val exchangeResult = _exchangeResult.asStateFlow()

    private val _baseCurrency = MutableStateFlow("USD")
    val baseCurrency = _baseCurrency.asStateFlow()

    private val _exchangeCurrency = MutableStateFlow("EUR")
    val exchangeCurrency = _exchangeCurrency.asStateFlow()

    init {
        //getExchange()
    }

    private fun getExchange() {
        viewModelScope.launch {
            exchangeRepo.getExchange().onSuccess {
                _exchangeResult.value = it

                _baseCurrency.value = it.rates.keys.elementAt(0)
                _exchangeCurrency.value = it.rates.keys.elementAt(1)
            }
        }
    }

    fun swapCurrencies() {
        _baseCurrency.value = _exchangeCurrency.value.also {
            _exchangeCurrency.value = _baseCurrency.value
        }

    }
}
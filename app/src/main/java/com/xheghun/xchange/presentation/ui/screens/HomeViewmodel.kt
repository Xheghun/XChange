package com.xheghun.xchange.presentation.ui.screens

import android.icu.text.DecimalFormat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xheghun.xchange.data.model.ExchangeResult
import com.xheghun.xchange.data.repo.ExchangeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val numberFormat = DecimalFormat("#,###.###")

class HomeViewmodel(private val exchangeRepo: ExchangeRepository) : ViewModel() {

    init {
        getExchange()
    }

    private val _exchangeResult = MutableStateFlow<ExchangeResult?>(null)
    val exchangeResult = _exchangeResult.asStateFlow()

    private val _exchangeTotal = MutableStateFlow("0.0")
    val exchangeTotal = _exchangeTotal.asStateFlow()

    private val _baseCurrency = MutableStateFlow("USD")
    val baseCurrency = _baseCurrency.asStateFlow()

    private val _exchangeCurrency = MutableStateFlow("EUR")
    val exchangeCurrency = _exchangeCurrency.asStateFlow()

    private val _baseCurrencyAmount = MutableStateFlow("1")
    val baseCurrencyAmount = _baseCurrencyAmount.asStateFlow()

    private val _exchangeCurrencyAmount = MutableStateFlow("1")
    val exchangeCurrencyAmount = _exchangeCurrencyAmount.asStateFlow()


    fun updateBaseCurrency(value: String) {
        if (value.isNotEmpty()) {
            _baseCurrency.value = value
            calculateExchange()
        }
    }

    fun updateExchangeCurrency(value: String) {
        if (value.isNotEmpty()) {
            _exchangeCurrency.value = value
            calculateExchange()
        }
    }

    fun updateBaseAmount(value: String) = updateAmount(value, _baseCurrencyAmount)
    fun updateExchangeAmount(value: String) = updateAmount(value, _exchangeCurrencyAmount)

    private fun getExchange() {
        viewModelScope.launch {
            exchangeRepo.getExchange().onSuccess {
                _exchangeResult.value = it

                _baseCurrency.value = it.rates.keys.elementAt(0)
                _exchangeCurrency.value = it.rates.keys.elementAt(1)

                calculateExchange()
            }
        }
    }

    fun swapCurrencies() {
        _baseCurrency.value = _exchangeCurrency.value.also {
            _exchangeCurrency.value = _baseCurrency.value
        }

        calculateExchange()
    }

    private fun updateAmount(value: String, updater: MutableStateFlow<String>) {
        if (value.isNotEmpty()) {
            updater.value = value.toInt().coerceAtLeast(1).toString()
            calculateExchange()
        }
    }

    private fun calculateExchange() {
        _exchangeResult.value?.rates?.let { rates ->
            val baseRate = rates[baseCurrency.value]
            val exchangeRate = rates[exchangeCurrency.value]

            if (baseRate != null && exchangeRate != null) {
                val conversionRate = exchangeRate / baseRate
                val result = (baseCurrencyAmount.value.toDouble() * conversionRate).format()

                _exchangeTotal.value = result
                _exchangeCurrencyAmount.value = result
            }
        }
    }
}

fun Any.format(): String {
    return try {
        numberFormat.format(this)
    } catch (e: Exception) {
        this.toString()
    }
}
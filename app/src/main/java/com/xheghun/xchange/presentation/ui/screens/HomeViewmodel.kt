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

    private val _baseCurrencyState =
        CurrencyState(
            MutableStateFlow("USD"),
            MutableStateFlow("1"),
        )

    private val _exchangeCurrencyState =
        CurrencyState(
            MutableStateFlow("EUR"),
            MutableStateFlow("1"),
        )


    val baseCurrency = _baseCurrencyState.currency.asStateFlow()
    val exchangeCurrency = _exchangeCurrencyState.currency.asStateFlow()

    val baseCurrencyAmount = _baseCurrencyState.amount.asStateFlow()
    val exchangeCurrencyAmount = _exchangeCurrencyState.amount.asStateFlow()


    fun updateBaseCurrency(value: String) {
        if (value.isNotEmpty()) {
            _baseCurrencyState.currency.value = value
            calculateExchange()
        }
    }

    fun updateExchangeCurrency(value: String) {
        if (value.isNotEmpty()) {
            _exchangeCurrencyState.currency.value = value
            calculateExchange()
        }
    }

    fun updateBaseAmount(value: String) = updateAmount(value, _baseCurrencyState.amount)
    fun updateExchangeAmount(value: String) = updateAmount(value, _exchangeCurrencyState.amount)

    fun swapCurrencies() {
        _baseCurrencyState.currency.value = _exchangeCurrencyState.currency.value.also {
            _exchangeCurrencyState.currency.value = _baseCurrencyState.currency.value
        }

        calculateExchange()
    }

    private fun updateAmount(value: String, updater: MutableStateFlow<String>) {
        if (value.isNotEmpty()) {
            updater.value = value.toInt().coerceAtLeast(1).toString()
            calculateExchange()
        }
    }

    private fun getExchange() {
        viewModelScope.launch {
            exchangeRepo.getExchange().onSuccess {
                _exchangeResult.value = it

                _baseCurrencyState.currency.value = it.rates.keys.elementAt(0)
                _exchangeCurrencyState.currency.value = it.rates.keys.elementAt(1)

                calculateExchange()
            }
        }
    }

    private fun calculateExchange() {
        _exchangeResult.value?.rates?.let { rates ->
            val baseRate = rates[baseCurrency.value]
            val exchangeRate = rates[exchangeCurrency.value]

            if (baseRate != null && exchangeRate != null) {
                val conversionRate = exchangeRate / baseRate
                val result = (_baseCurrencyState.amount.value.toDouble() * conversionRate).format()

                _exchangeTotal.value = result
                _exchangeCurrencyState.amount.value = result
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
package com.xheghun.xchange.data.model

data class ExchangeResult(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)

fun ExchangeResult.toJsonString() {

}
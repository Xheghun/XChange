package com.xheghun.xchange.data.cache

interface Cache<T> {
    suspend fun get(key: String): T?
    suspend fun put(key: String, value: T)
    suspend fun remove(key: String)
}
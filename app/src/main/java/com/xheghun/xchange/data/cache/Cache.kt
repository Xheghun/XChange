package com.xheghun.xchange.data.cache

interface Cache<T> {
    fun get(): T?
    fun put(key: String, value: T)
}
package com.xheghun.xchange.data.cache

class LocalCache<T> : Cache<T> {
    override fun get(): T? {
        return null
    }

    override fun put(key: String, value: T) {

    }
}
package com.xheghun.xchange.data.cache

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import okio.IOException

class LocalCache<T>(
    private val context: Context,
    private val gson: Gson,
    private val clazz: Class<T>,
    private val timeToLive: Long
) : Cache<T> {
    private val Context.dataStore by preferencesDataStore(name = "local_cache")

    override suspend fun get(key: String): T? {
        try {
            val preference = context.dataStore.data.first()
            val value = stringPreferencesKey(key)
            val timestampKey = longPreferencesKey("$key-timestamp")

            val cacheValue = preference[value]
            val cacheTime = preference[timestampKey]

            if (cacheValue != null && cacheTime != null) {
                if (System.currentTimeMillis() - cacheTime <= timeToLive) {
                    return gson.fromJson(cacheValue, clazz)
                }
            } else {
                //clear expired cache
                remove(key)
            }
            return null
        } catch (e: IOException) {
            return null
        }
    }

    override suspend fun put(key: String, value: T) {
        runCatching {
            context.dataStore.edit { preference ->
                val valueKey = stringPreferencesKey(key)
                val timestampKey = longPreferencesKey("$key-timestamp")

                preference[valueKey] = gson.toJson(value)
                preference[timestampKey] = System.currentTimeMillis()
            }
        }
    }

    override suspend fun remove(key: String) {
        runCatching {
            context.dataStore.edit { preference ->
                val valueKey = stringPreferencesKey(key)
                val timestampKey = longPreferencesKey("$key-timestamp")

                preference.remove(valueKey)
                preference.remove(timestampKey)
            }
        }
    }
}
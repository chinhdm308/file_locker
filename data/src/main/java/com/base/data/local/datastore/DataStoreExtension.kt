package com.base.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

suspend fun <T> DataStore<Preferences>.put(key: Preferences.Key<T>, value: T) {
    edit { settings ->
        settings.putAll()
        settings[key] = value
    }
}

suspend inline fun <reified T> DataStore<Preferences>.get(key: Preferences.Key<T>): T {
    return data.map { preferences ->
        preferences[key] ?: defaultValue()
    }.first()
}

/**
 * using Gson to parser Object to String
 */
inline fun <reified T> defaultValue(): T = when (T::class) {
    Boolean::class -> false as T
    Int::class -> 0 as T
    Long::class -> 0L as T
    Float::class -> 0f as T
    String::class -> "" as T
    Set::class -> mutableSetOf<String>() as T
    else -> throw IllegalStateException("Type value not support")
}
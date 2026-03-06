package com.litvy.litvysales.domain.interfaces.config

interface AppConfigRepository {

    suspend fun getValue(key: String): String?

    suspend fun setValue(key: String, value: String)
}
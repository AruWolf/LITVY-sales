package com.litvy.litvysales.data.repository.util

import com.litvy.litvysales.data.local.dao.util.AppConfigDao
import com.litvy.litvysales.data.mapper.util.toEntity
import com.litvy.litvysales.domain.interfaces.config.AppConfigRepository
import com.litvy.litvysales.domain.model.util.AppConfig

class AppConfigRepositoryImpl(
    private val dao: AppConfigDao
) : AppConfigRepository {

    override suspend fun getValue(key: String): String {
        return dao.getById(key).value
    }

    override suspend fun setValue(key: String, value: String) {
        dao.insert(
            AppConfig(key, value, System.currentTimeMillis()).toEntity()
        )
    }
}
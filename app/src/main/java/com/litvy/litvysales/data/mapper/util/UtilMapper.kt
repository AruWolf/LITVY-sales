package com.litvy.litvysales.data.mapper.util

import com.litvy.litvysales.data.local.entity.util.*
import com.litvy.litvysales.domain.model.util.*

fun AppConfigEntity.toDomain() = AppConfig(
    key,
    value,
    updatedAt
)

fun AppConfig.toEntity() = AppConfigEntity(
    key,
    value,
    updatedAt
)

fun PaymentMethodEntity.toDomain() = PaymentMethod(
    id,
    name,
    surchargePercentage
)

fun PaymentMethod.toEntity() = PaymentMethodEntity(
    id,
    name,
    surchargePercentage
)
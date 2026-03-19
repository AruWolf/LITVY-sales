package com.litvy.litvysales.domain.model.purchases

data class ProviderVisitDay(
    val providerId: Int,
    val dayOfWeek: Int
)

data class ProviderByVisitDay(
    val dayOfWeek: Int,
    val provider: Provider
)
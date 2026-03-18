package com.litvy.litvysales.domain.model.purchases

data class ProviderWithVisitDays(
    val provider: Provider,
    val visitDays: Set<Int>
)

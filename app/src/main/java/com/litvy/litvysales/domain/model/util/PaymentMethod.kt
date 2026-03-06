package com.litvy.litvysales.domain.model.util

data class PaymentMethod(
    val id: Int = 0,

    val name: String,

    val surchargePercentage: Double
)

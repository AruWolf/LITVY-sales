package com.litvy.litvysales.data.local.entity

import androidx.room.*

@Entity(tableName = "paymentMethod")
data class PaymentMethodEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val surchargePercentage: Double
)
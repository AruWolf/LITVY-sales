package com.litvy.litvysales.domain.model.inventory

import com.litvy.litvysales.data.local.entity.enums.StockMovementType

data class StockMovement(
    val id: Int? = 0,

    val productId: Int,

    val batchId: Int?,

    val type: StockMovementType,

    val quantity: Double,

    val createdAt: Long,

    val referenceId: Int?,
    val referenceType: String?,

    val createdBy: Int
)
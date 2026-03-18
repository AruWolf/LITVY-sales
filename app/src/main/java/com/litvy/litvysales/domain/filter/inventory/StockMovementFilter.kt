package com.litvy.litvysales.domain.filter.inventory

import com.litvy.litvysales.domain.model.enums.StockMovementType

data class StockMovementFilter(
    val productId: Int? = null,
    val batchId: Int? = null,
    val type: StockMovementType? = null,
    val createdAt: Long? = null,
    val referenceId: Int? = null,
    val referenceType: String? = null,
    val createdBy: Int? = null
)

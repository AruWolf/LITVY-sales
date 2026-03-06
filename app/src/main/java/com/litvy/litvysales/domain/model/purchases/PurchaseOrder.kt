package com.litvy.litvysales.domain.model.purchases

import com.litvy.litvysales.data.local.entity.enums.PurchaseOrderStatus

data class PurchaseOrder(
    val id: Int = 0,
    val providerId: Int,
    val status: PurchaseOrderStatus,
    val expectedDeliveryDate: Long?,
    val createdAt: Long,
    val createdBy: Int
)

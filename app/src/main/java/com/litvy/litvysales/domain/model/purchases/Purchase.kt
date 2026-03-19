package com.litvy.litvysales.domain.model.purchases

data class Purchase(
    val id: Int = 0,
    val providerId: Int,
    val salesRepName: String? = null,
    val invoiceTypeId: Int,
    val paymentMethodId: Int,
    val subtotalInCents: Long,
    val totalDiscountInCents: Long,
    val totalTaxInCents: Long,
    val totalInCents: Long,
    val createdAt: Long,
    val createdBy: Int
)

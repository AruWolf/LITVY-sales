package com.litvy.litvysales.domain.model.sales

data class SalePayment(
    val id: Int = 0,
    val saleId: Int,
    val paymentMethodId: Int,
    val amountInCents: Long
)
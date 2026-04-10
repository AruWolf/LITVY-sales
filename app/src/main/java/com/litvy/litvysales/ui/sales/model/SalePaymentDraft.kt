package com.litvy.litvysales.ui.sales.model

data class SalePaymentDraft(
    val paymentMethodId: Int,
    val name: String,
    val amount: Long
)
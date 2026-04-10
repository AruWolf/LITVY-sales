package com.litvy.litvysales.ui.sales.model

data class SaleDraft(
    val items: List<SaleItemDraft> = emptyList(),
    val payments: List<SalePaymentDraft> = emptyList(),
    val subtotal: Long = 0,
    val surcharge: Long = 0,
    val total: Long = 0
)

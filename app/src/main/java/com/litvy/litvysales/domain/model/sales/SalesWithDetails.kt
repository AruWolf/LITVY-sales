package com.litvy.litvysales.domain.model.sales

data class SaleWithDetails(
    val sale: Sale,
    val items: List<SaleItem>,
    val payments: List<SalePayment>
)
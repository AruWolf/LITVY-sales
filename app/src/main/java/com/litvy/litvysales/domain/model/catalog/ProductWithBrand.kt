package com.litvy.litvysales.domain.model.catalog

data class ProductWithBrand(
    val id: Int,
    val name: String,
    val brandId: Int,
    val brandName: String,
    val purchasePriceInCents: Long,
    val salePriceInCents: Long,
    val hasExpiration: Boolean,
    val isWeighable: Boolean,
    var active: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)

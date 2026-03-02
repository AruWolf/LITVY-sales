package com.litvy.litvysales.domain.model.catalog

data class Product(
    val id: Int?,
    val name: String,
    val brandId: Int,
    val purchasePriceInCents: Long,
    val salePriceInCents: Long,
    val hasExpiration: Boolean,
    val isWeighable: Boolean,
    val active: Boolean,
    val createdAt: Long,
    val updatedAt: Long
)
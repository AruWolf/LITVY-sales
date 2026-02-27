package com.litvy.litvysales.data.local.projection

// Concepto de DTO de consulta. Se encarga de traer el producto en conjunto con toda la jerarquia que tiene(Marca, Subcategoria, Categoria)
data class ProductFullProjection(
    val id: Int,
    val name: String,

    val brandName: String,
    val subCategoryName: String,
    val categoryName: String,

    val salePriceInCents: Long,
    val active: Boolean
)
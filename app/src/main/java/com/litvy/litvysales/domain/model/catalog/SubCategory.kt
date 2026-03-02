package com.litvy.litvysales.domain.model.catalog

data class SubCategory(
    val id: Int?,
    val categoryId: Int,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long
)
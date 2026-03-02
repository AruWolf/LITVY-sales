package com.litvy.litvysales.domain.model.catalog

data class Brand(
    val id: Int?,
    val name: String,
    val subCategoryId: Int,
    val createdAt: Long,
    val updatedAt: Long
)
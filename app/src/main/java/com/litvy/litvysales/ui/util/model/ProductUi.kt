package com.litvy.litvysales.ui.util.model

data class ProductUi(

    val id: Int,

    val name: String,

    val barcode: String?,

    val purchasePrice: Long,

    val salePrice: Long? = null,

    val categoryId: Int,

    val categoryName: String,

    val subCategoryId: Int,

    val subCategoryName: String,

    val brandId: Int,

    val brandName: String,

    val active: Boolean = true
)

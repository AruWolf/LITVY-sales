package com.litvy.litvysales.ui.catalog.model

data class ProductFormState(

    val name: String = "",
    val purchase: String = "",
    val sale: String = "",
    val hasExpiration: Boolean = false,
    val isWeighable: Boolean = false

)
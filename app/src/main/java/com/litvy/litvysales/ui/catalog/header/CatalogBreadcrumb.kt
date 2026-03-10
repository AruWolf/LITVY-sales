package com.litvy.litvysales.ui.catalog.header


data class CatalogBreadcrumbItem(
    val title: String,
    val onClick: (() -> Unit)? = null
)
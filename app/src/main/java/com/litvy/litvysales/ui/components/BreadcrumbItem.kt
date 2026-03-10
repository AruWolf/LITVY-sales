package com.litvy.litvysales.ui.components

data class BreadcrumbItem(
    val label: String,
    val onClick: (() -> Unit)? = null
)
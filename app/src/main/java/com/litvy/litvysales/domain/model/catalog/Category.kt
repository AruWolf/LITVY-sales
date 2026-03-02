package com.litvy.litvysales.domain.model.catalog

import java.time.LocalDateTime

data class Category(
    val id: Int?,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long
)
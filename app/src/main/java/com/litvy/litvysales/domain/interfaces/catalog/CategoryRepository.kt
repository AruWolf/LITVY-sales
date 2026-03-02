package com.litvy.litvysales.domain.interfaces.catalog

import com.litvy.litvysales.domain.model.catalog.Category

interface CategoryRepository {

    suspend fun create(category: Category)

    suspend fun update(category: Category)

    suspend fun getById(id: Int): Category?

    suspend fun getAll(): List<Category>

    suspend fun existsByName(name: String): Boolean

    suspend fun hasSubCategories(categoryId: Int): Boolean
}
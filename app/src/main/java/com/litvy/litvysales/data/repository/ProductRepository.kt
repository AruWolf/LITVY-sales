package com.litvy.litvysales.data.repository

import com.litvy.litvysales.data.local.dao.ProductDao
import com.litvy.litvysales.data.local.entity.ProductEntity

class ProductRepository(
    private val productDao: ProductDao
) {

    suspend fun create(product: ProductEntity) {
        productDao.insert(product)
    }

    fun getActive() = productDao.getActiveProducts()
}
package com.litvy.litvysales.catalog.product

import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository
import com.litvy.litvysales.domain.model.catalog.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeProductRepository : ProductRepository {

    private val products = mutableListOf<Product>()

    override suspend fun insert(product: Product) {
        products.add(product)
    }

    override suspend fun update(product: Product) {
        val index = products.indexOfFirst { it.id == product.id }
        if (index != -1) {
            products[index] = product
        }
    }

    override fun getActiveProducts(): Flow<List<Product>> {
        return flowOf(products.filter { it.active })
    }

    override fun getByBrand(brandId: Int): Flow<List<Product>> {
        return flowOf(products.filter { it.brandId == brandId })
    }

    override fun searchByName(query: String): Flow<List<Product>> {
        return flowOf(products.filter { it.name.contains(query, true) })
    }

    override suspend fun getProductWithBrand(productId: Int) {}

    override suspend fun countByBrand(brandId: Int): Int {
        return products.count { it.brandId == brandId }
    }

    override suspend fun deactivate(productId: Int, updatedAt: Long) {
        val index = products.indexOfFirst { it.id == productId }
        if (index != -1) {
            val product = products[index]
            products[index] = product.copy(active = false, updatedAt = updatedAt)
        }
    }

    override suspend fun getProductFull(productId: Int): Product {
        return products.first { it.id == productId }
    }

    override suspend fun getById(productId: Int): Product? {
        return products.find { it.id == productId }
    }

    override suspend fun existsByNameInBrandExcludingId(
        name: String,
        brandId: Int,
        productId: Int
    ): Boolean {
        return products.any {
            it.name == name && it.brandId == brandId && it.id != productId
        }
    }

    override suspend fun existsByNameInBrand(name: String, brandId: Int): Boolean {
        return products.any {
            it.name == name && it.brandId == brandId
        }
    }
}
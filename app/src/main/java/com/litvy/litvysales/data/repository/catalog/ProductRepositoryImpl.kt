package com.litvy.litvysales.data.repository.catalog

import com.litvy.litvysales.data.local.dao.catalog.ProductDao
import com.litvy.litvysales.data.mapper.catalog.toDomain
import com.litvy.litvysales.data.mapper.catalog.toEntity
import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository
import com.litvy.litvysales.domain.model.catalog.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productDao: ProductDao
): ProductRepository {
    override suspend fun insert(product: Product) {
        productDao.insert(product.toEntity())
    }

    override suspend fun update(product: Product) {
        productDao.update(product.toEntity())
    }

    override fun getActiveProducts(): Flow<List<Product>> {
        return productDao.getActiveProducts()
            .map { list -> list.map { it.toDomain() } }
    }

    override fun getByBrand(brandId: Int): Flow<List<Product>> {
        return productDao.getByBrand(brandId)
            .map { list -> list.map {it.toDomain()} }
    }

    override fun searchByName(query: String): Flow<List<Product>> {
        return productDao.searchByName(query)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getProductWithBrand(productId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun countByBrand(brandId: Int): Int {
        return productDao.countByBrand(brandId)
    }

    override suspend fun deactivate(productId: Int, updatedAt: Long) {
        productDao.deactivate(productId, updatedAt)
    }

    override suspend fun activate(productId: Int, updatedAt: Long) {
        productDao.activate(productId, updatedAt)
    }

    override suspend fun getProductFull(productId: Int): Product {
        TODO("Not yet implemented")
    }

    override suspend fun getById(productId: Int): Product? {
        return productDao.getById(productId)?.toDomain()
    }

    override suspend fun existsByNameInBrandExcludingId(name: String, brandId: Int, productId: Int): Boolean {
        return productDao.countByNameExcludingId(name, brandId, productId) > 0
    }

    override suspend fun existsByNameInBrand(name: String, brandId: Int): Boolean {
        return productDao.countByName(name, brandId) > 0
    }

    override suspend fun existsById(id: Int): Boolean {
        return productDao.existsById(id) > 0
    }

}
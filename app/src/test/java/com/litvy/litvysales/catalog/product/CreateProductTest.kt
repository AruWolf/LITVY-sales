package com.litvy.litvysales.catalog.product

import com.litvy.litvysales.domain.model.catalog.Product
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreateProductTest {
    private val productRepository = FakeProductRepository()

    @Test
    fun should_create_product() = runBlocking {

        val product = Product(
            id = 1,
            name = "Coca Cola 500ml",
            brandId = 1,
            purchasePriceInCents = 1000,
            salePriceInCents = 1500,
            active = true,
            hasExpiration = true,
            isWeighable = false,
            createdAt = 0,
            updatedAt = 0
        )

        productRepository.insert(product)

        val result = productRepository.getById(1)

        assertEquals("Coca Cola 500ml", result?.name)
    }
}
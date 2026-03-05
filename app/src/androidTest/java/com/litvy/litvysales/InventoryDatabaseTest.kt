package com.litvy.litvysales

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.litvy.litvysales.data.local.database.AppDatabase
import com.litvy.litvysales.data.local.entity.catalog.BrandEntity
import com.litvy.litvysales.data.local.entity.catalog.CategoryEntity
import com.litvy.litvysales.data.local.entity.catalog.ProductEntity
import com.litvy.litvysales.data.local.entity.catalog.SubCategoryEntity
import com.litvy.litvysales.data.local.entity.enums.StockMovementType
import com.litvy.litvysales.data.local.entity.inventory.InventoryEntity
import com.litvy.litvysales.data.local.entity.inventory.StockBatchEntity
import com.litvy.litvysales.data.local.entity.inventory.StockMovementEntity
import com.litvy.litvysales.data.local.entity.user.RoleEntity
import com.litvy.litvysales.data.local.entity.user.UserEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InventoryDatabaseTest {

    private lateinit var database: AppDatabase

    @Before
    fun setup() {
        println("=== SETUP START ===")

        try {
            database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()

            println("=== DB CREATED ===")

        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }

        runBlocking {
            insertMinimalStructure()
        }

        println("=== SETUP END ===")
    }

    @After
    fun tearDown() {
        database.close()
    }

    private suspend fun insertMinimalStructure() {

        val now = System.currentTimeMillis()

        val category = CategoryEntity(
            id = 1,
            name = "Cat",
            createdAt = now,
            updatedAt = now
        )

        val subCategory = SubCategoryEntity(
            id = 1,
            name = "Sub",
            categoryId = 1,
            createdAt = now,
            updatedAt = now
        )

        val brand = BrandEntity(
            id = 1,
            name = "Brand",
            subCategoryId = 1,
            createdAt = now,
            updatedAt = now
        )

        val product = ProductEntity(
            id = 1,
            name = "Product",
            brandId = 1,
            purchasePriceInCents = 1000,
            salePriceInCents = 1500,
            hasExpiration = false,
            isWeighable = false,
            active = true,
            createdAt = now,
            updatedAt = now
        )

        val role = RoleEntity(
            id = 1,
            name = "Admin",
        )

        val user = UserEntity(
            id = 1,
            name = "User",
            lastname = "user",
            email = "test@test.com",
            telephoneNumber = null,
            dni = "12345678",
            passwordHash = "1234",
            birthDate = null,
            address = null,
            roleId = 1,
            active = true,
            createdAt = now,
            updatedAt = now
        )

        database.categoryDao().insert(category)
        database.subCategoryDao().insert(subCategory)
        database.brandDao().insert(brand)
        database.productDao().insert(product)
        database.roleDao().insert(role)
        database.userDao().insert(user)
    }

    @Test
    fun insertAndReadInventory() = runBlocking {

        val inventory = InventoryEntity(
            productId = 1,
            stock = 20.0,
            updatedAt = System.currentTimeMillis()
        )

        database.inventoryDao().insert(inventory)

        val result = database.inventoryDao()
            .getByProductId(1)
            .first()

        Assert.assertNotNull(result)
        Assert.assertEquals(20.0, result?.stock)
    }

    @Test
    fun insertBatchAndVerifyOrderByExpiration() = runBlocking {

        val batch1 = StockBatchEntity(
            id = 0,
            productId = 1,
            quantity = 5.0,
            expirationDate = 2000L,
            purchaseItemId = null,
            createdAt = System.currentTimeMillis()
        )

        val batch2 = StockBatchEntity(
            id = 0,
            productId = 1,
            quantity = 5.0,
            expirationDate = 1000L,
            purchaseItemId = null,
            createdAt = System.currentTimeMillis()
        )

        database.stockBatchDao().loadBatch(batch1)
        database.stockBatchDao().loadBatch(batch2)

        val result = database.stockBatchDao()
            .getBatchByProduct(1)
            .first()

        Assert.assertEquals(2, result.size)
        Assert.assertTrue(result[0].expirationDate!! <= result[1].expirationDate!!)
    }

    @Test
    fun insertMovementAndReadByProduct() = runBlocking {

        val movement = StockMovementEntity(
            id = 0,
            productId = 1,
            batchId = null,
            type = StockMovementType.PURCHASE,
            quantity = 10.0,
            createdAt = System.currentTimeMillis(),
            referenceId = null,
            referenceType = null,
            createdBy = 1
        )

        database.stockMovementDao().generateMovement(movement)

        val result = database.stockMovementDao()
            .getStockMovementByProduct(1)
            .first()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals(10.0, result[0].quantity)
    }
}
package com.litvy.litvysales.data.local.dao.catalog

import androidx.room.*
import com.litvy.litvysales.data.local.entity.catalog.ProductEntity
import com.litvy.litvysales.data.local.relation.ProductWithBrand
import com.litvy.litvysales.data.local.projection.ProductFullProjection
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(product: ProductEntity)

    @Update
    suspend fun update(product: ProductEntity)

    @Query("""
        SELECT * FROM product
        WHERE active = 1
        ORDER BY name ASC
    """)
    fun getActiveProducts(): Flow<List<ProductEntity>>

    @Query("""
        SELECT * FROM product
        WHERE brandId = :brandId
        ORDER BY name ASC
    """)
    fun getByBrand(brandId: Int): Flow<List<ProductEntity>>

    @Query("SELECT * FROM product WHERE id = :productId")
    suspend fun getById(productId: Int): ProductEntity?

    @Query("""
        SELECT * FROM product
        WHERE name LIKE '%' || :query || '%'
    """)
    fun searchByName(query: String): Flow<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM product WHERE name = :name AND brandId = :brandId AND id != :productId")
    suspend fun countByNameExcludingId(name: String, brandId: Int,productId: Int): Int

    @Query("SELECT COUNT(*) FROM product WHERE name = :name AND brandId = :brandId")
    suspend fun countByName(name: String, brandId: Int): Int

    @Transaction
    @Query("SELECT * FROM product WHERE id = :productId")
    suspend fun getProductWithBrand(productId: Int): ProductWithBrand?

    @Query("SELECT COUNT(*) FROM product WHERE brandId = :brandId")
    suspend fun countByBrand(brandId: Int): Int

    @Query("""
    UPDATE product
    SET active = 0,
        updatedAt = :updatedAt
    WHERE id = :id
""")
    suspend fun deactivate(id: Int, updatedAt: Long)

    // Concepto de consulta utilizando codigo SQL y un DTO constructor de consulta
    @Query("""
    SELECT 
        p.id,
        p.name,
        b.name AS brandName,
        sc.name AS subCategoryName,
        c.name AS categoryName,
        p.salePriceInCents,
        p.active
    FROM product p
    INNER JOIN brand b ON p.brandId = b.id
    INNER JOIN subCategory sc ON b.subCategoryId = sc.id
    INNER JOIN category c ON sc.categoryId = c.id
    WHERE p.id = :productId
""")
    suspend fun getProductFull(productId: Int): ProductFullProjection?
}
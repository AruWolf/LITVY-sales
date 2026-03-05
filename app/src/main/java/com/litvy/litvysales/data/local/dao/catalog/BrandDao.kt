package com.litvy.litvysales.data.local.dao.catalog

import androidx.room.*
import com.litvy.litvysales.data.local.entity.catalog.BrandEntity

@Dao
interface BrandDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(brand: BrandEntity)

    @Update
    suspend fun update(brand: BrandEntity)

    @Query("""
    SELECT * FROM brand
    WHERE subCategoryId = :subCategoryId
    """)
    suspend fun getBySubCategory(subCategoryId: Int): List<BrandEntity>

    @Query("SELECT * FROM brand WHERE brand.id = :brandId")
    suspend fun getById(brandId: Int): BrandEntity?

    @Query("SELECT COUNT(*) FROM brand WHERE name = :name AND subCategoryId = :subCategoryId")
    suspend fun countByNameInSubCategory(name: String, subCategoryId: Int): Int

    @Query("SELECT COUNT(*) FROM brand WHERE subCategoryId = :subCategoryId")
    suspend fun countBySubCategory(subCategoryId: Int): Int
}
package com.litvy.litvysales.data.local.dao

import androidx.room.*
import com.litvy.litvysales.data.local.entity.catalog.BrandEntity

@Dao
interface BrandDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(brand: BrandEntity)

    @Query("""
    SELECT * FROM brand
    WHERE subCategoryId = :subCategoryId
""")
    suspend fun getBySubCategory(subCategoryId: Int): List<BrandEntity>

    @Query("SELECT * FROM brand WHERE name = :name AND subCategoryId = :subCategoryId")
    suspend fun existsByNameInSubCategory(name: String, subCategoryId: Int): Boolean

    @Query("SELECT COUNT(*) FROM brand WHERE subCategoryId = :subCategoryId")
    suspend fun countBySubCategory(subCategoryId: Int): Int
}
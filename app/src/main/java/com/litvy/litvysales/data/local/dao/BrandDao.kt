package com.litvy.litvysales.data.local.dao

import androidx.room.*
import com.litvy.litvysales.data.local.entity.BrandEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BrandDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(brand: BrandEntity)

    @Query("""
        SELECT * FROM brand
        WHERE subCategoryId = :subCategoryId
        ORDER BY name ASC
    """)
    fun getBySubCategory(subCategoryId: Int): Flow<List<BrandEntity>>
}
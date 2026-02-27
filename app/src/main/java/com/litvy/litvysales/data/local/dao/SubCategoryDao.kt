package com.litvy.litvysales.data.local.dao

import androidx.room.*
import com.litvy.litvysales.data.local.entity.SubCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubCategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subCategory: SubCategoryEntity)

    @Query("""
        SELECT * FROM subCategory 
        WHERE categoryId = :categoryId
        ORDER BY name ASC
    """)
    fun getByCategory(categoryId: Int): Flow<List<SubCategoryEntity>>
}
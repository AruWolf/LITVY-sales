package com.litvy.litvysales.data.local.dao.catalog

import androidx.room.*
import com.litvy.litvysales.data.local.entity.catalog.SubCategoryEntity

@Dao
interface SubCategoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: SubCategoryEntity): Long

    @Update
    suspend fun update(entity: SubCategoryEntity)

    @Query("""
        SELECT * FROM subCategory
        WHERE categoryId = :categoryId
        ORDER BY name ASC
    """)
    suspend fun getByCategory(categoryId: Int): List<SubCategoryEntity>

    @Query("SELECT * FROM subCategory WHERE id = :id ")
    suspend fun getById(id: Int): SubCategoryEntity?

    @Query("SELECT COUNT(*) FROM subCategory WHERE categoryId = :categoryId")
    suspend fun countByCategory(categoryId: Int): Int

    @Query("SELECT COUNT(*) FROM subCategory WHERE name = :name AND categoryId = :categoryId")
    suspend fun countByName(name: String, categoryId: Int): Int
}
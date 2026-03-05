package com.litvy.litvysales.data.local.dao.catalog

import androidx.room.*
import com.litvy.litvysales.data.local.entity.catalog.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(category: CategoryEntity): Long

    @Update
    suspend fun update(category: CategoryEntity)

    @Query("SELECT * FROM category ORDER BY name ASC")
    suspend fun getAll(): List<CategoryEntity>

    @Query("SELECT * FROM category WHERE id = :id")
    suspend fun getById(id: Int): CategoryEntity?

    @Query("SELECT COUNT(*) FROM category WHERE id = :id")
    suspend fun countById(id: Int): Int

    @Query("SELECT COUNT(*) FROM category WHERE name = :name")
    suspend fun countByName(name: String): Int
}
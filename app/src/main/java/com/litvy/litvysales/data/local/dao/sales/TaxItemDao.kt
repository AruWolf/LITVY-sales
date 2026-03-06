package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.sales.TaxItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaxItemDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(taxItem: TaxItemEntity)

    @Query("SELECT * FROM taxItem WHERE id = :id")
    suspend fun getById(id : Int): TaxItemEntity?

    @Query("SELECT * FROM taxItem ORDER BY id ASC")
    fun getAll(): Flow<List<TaxItemEntity?>>
}
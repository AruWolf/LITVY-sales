package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.sales.SaleEntity

interface SaleDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(sale: SaleEntity)

    @Update
    suspend fun update(sale: SaleEntity)



}
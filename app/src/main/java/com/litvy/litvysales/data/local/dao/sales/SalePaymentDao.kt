package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.sales.SalePaymentEntity

@Dao
interface SalePaymentDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(salePayment: SalePaymentEntity)

    @Query("SELECT * FROM salePayment WHERE id = :id")
    suspend fun getById(id: Int): SalePaymentEntity?



}
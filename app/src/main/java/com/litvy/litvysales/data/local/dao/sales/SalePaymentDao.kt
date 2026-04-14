package com.litvy.litvysales.data.local.dao.sales

import androidx.room.*
import com.litvy.litvysales.data.local.entity.sales.SalePaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SalePaymentDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(salePayment: SalePaymentEntity): Long

    @Update
    suspend fun update(salePayment: SalePaymentEntity)

    @Query("SELECT * FROM salePayment WHERE id = :id")
    suspend fun getById(id: Int): SalePaymentEntity?

    @Query("SELECT * FROM salePayment WHERE saleId = :saleId")
    fun getBySale(saleId: Int): Flow<List<SalePaymentEntity>>

    @Query("SELECT * FROM salePayment")
    fun getAll(): Flow<List<SalePaymentEntity>>

}

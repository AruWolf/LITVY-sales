package com.litvy.litvysales.data.local.dao.purchases

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.purchases.PurchaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(purchase: PurchaseEntity): Long

    @Update
    suspend fun update(purchase: PurchaseEntity)

    @Delete
    suspend fun delete(id: Int)

    @Query("SELECT * FROM purchase WHERE id = :id")
    suspend fun getById(id: Int): PurchaseEntity?

    @Query("SELECT * FROM purchase WHERE providerId = :providerId")
    fun getByProvider(providerId: Int): Flow<List<PurchaseEntity>>

    @Query("SELECT * FROM purchase WHERE invoiceTypeId = :invoiceTypeId ORDER BY createdAt DESC")
    fun getByInvoiceType(invoiceTypeId: Int): Flow<List<PurchaseEntity?>>

    @Query("SELECT * FROM purchase WHERE paymentMethodId = :paymentMethodId ORDER BY createdAt DESC")
    fun getByPaymentMethod(paymentMethodId: Int): Flow<List<PurchaseEntity?>>

    @Query("SELECT * FROM purchase WHERE createdAt = :createdAt ORDER BY id ASC")
    fun getByCreatedDay(createdAt: Long): Flow<List<PurchaseEntity?>>
}
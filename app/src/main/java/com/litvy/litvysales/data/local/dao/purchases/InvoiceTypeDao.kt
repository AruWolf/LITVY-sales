package com.litvy.litvysales.data.local.dao.purchases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.purchases.InvoiceTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceTypeDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(invoiceType: InvoiceTypeEntity)

    @Query("SELECT * FROM invoice_type WHERE id = :id")
    suspend fun getById(id: Int): InvoiceTypeEntity?

    @Query("SELECT * FROM invoice_type")
    fun getAll(): Flow<List<InvoiceTypeEntity?>>
}
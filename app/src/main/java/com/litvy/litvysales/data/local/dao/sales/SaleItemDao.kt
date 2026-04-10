package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.sales.SaleEntity
import com.litvy.litvysales.data.local.entity.sales.SaleItemEntity
import com.litvy.litvysales.data.local.entity.util.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleItemDao{

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(saleItem: SaleItemEntity)

    @Query("SELECT * FROM saleItem WHERE id = :id")
    suspend fun getById(id: Int): SaleItemEntity?

    @Query("SELECT * FROM saleItem WHERE saleId = :saleId")
    fun getBySale(saleId: Int): Flow<List<SaleItemEntity>>

    @Query("SELECT * FROM saleItem WHERE productId = :productId")
    fun getByProduct(productId: Int): Flow<List<SaleItemEntity>>

    //TODO: Realizar consulta compleja de venta según tipo de pago
    //fun getByPaymentMethod(paymentMethodEntity: PaymentMethodEntity): Flow<List<SaleEntity?>>


}
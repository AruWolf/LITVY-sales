package com.litvy.litvysales.data.local.dao.purchases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.purchases.PurchaseOrderItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseOrderItemDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(purchaseOrderItem: PurchaseOrderItemEntity)


    @Query("SELECT * FROM purchaseOrderItem WHERE id = :id")
    suspend fun getById(id: Int): PurchaseOrderItemEntity?

    @Query("SELECT * FROM purchaseOrderItem WHERE purchaseOrderId = :orderId")
    fun getByOrderId(orderId: Int): Flow<List<PurchaseOrderItemEntity>>
}

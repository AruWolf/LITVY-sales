package com.litvy.litvysales.data.local.dao.purchases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.enums.PurchaseOrderStatus
import com.litvy.litvysales.data.local.entity.purchases.PurchaseOrderEntity
import com.litvy.litvysales.data.local.entity.purchases.PurchaseOrderItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseOrderDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(purchaseOrder: PurchaseOrderEntity): Long

    @Transaction
    suspend fun insertWithItems(purchaseOrder: PurchaseOrderEntity, items: List<PurchaseOrderItemEntity>){
        val purchaseOrderId = insert(purchaseOrder)

        val itemsWithPurchasedId = items.map {
            it.copy(purchaseOrderId = purchaseOrderId.toInt())
        }

        insertItems(itemsWithPurchasedId)
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertItems(items: List<PurchaseOrderItemEntity>)

    @Update
    suspend fun update(purchaseOrder: PurchaseOrderEntity)

    @Transaction
    suspend fun updateWithItems(
        purchaseOrder: PurchaseOrderEntity,
        items: List<PurchaseOrderItemEntity>
    ) {
        update(purchaseOrder)

        deleteItemsByOrder(purchaseOrder.id)

        val itemsWithOrderId = items.map {
            it.copy(purchaseOrderId = purchaseOrder.id)
        }

        insertItems(itemsWithOrderId)
    }

    @Query("DELETE FROM purchaseOrderItem WHERE purchaseOrderId = :orderId")
    suspend fun deleteItemsByOrder(orderId: Int)

    @Query("SELECT * FROM purchaseOrder WHERE id = :id")
    suspend fun getById(id: Int): PurchaseOrderEntity?

    @Query("SELECT * FROM purchaseOrder")
    suspend fun getAll(): Flow<List<PurchaseOrderEntity?>>

    @Query("SELECT * FROM purchaseOrder WHERE providerId = :providerId")
    fun getByProvider(providerId: Int): Flow<List<PurchaseOrderEntity?>>

    @Query("SELECT * FROM purchaseOrder WHERE status = :status ORDER BY createdAt DESC")
    fun getByStatus(status: PurchaseOrderStatus): Flow<List<PurchaseOrderEntity?>>

    @Query("SELECT * FROM purchaseOrder WHERE expectedDeliveryDate = :expectedDeliveryDate ORDER BY expectedDeliveryDate ASC")
    fun getByExpectedDeliveryDate(expectedDeliveryDate: Long): Flow<List<PurchaseOrderEntity?>>

    @Query("SELECT * FROM purchaseOrder WHERE createdAt = :createdAt ORDER BY createdAt ASC")
    fun getByCreationDate(createdAt: Long): Flow<List<PurchaseOrderEntity?>>

    @Query("SELECT * FROM purchaseOrder WHERE createdBy = :userId ORDER BY createdAt DESC")
    fun getByUser(userId: Int): Flow<List<PurchaseOrderEntity?>>


}
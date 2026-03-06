package com.litvy.litvysales.data.local.dao.purchases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.enums.PurchaseOrderStatus
import com.litvy.litvysales.data.local.entity.purchases.PurchaseOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseOrderDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(purchaseOrder: PurchaseOrderEntity)

    @Update
    suspend fun update(purchaseOrder: PurchaseOrderEntity)

    @Query("SELECT * FROM purchaseOrder WHERE id = :id")
    suspend fun getById(id: Int): PurchaseOrderEntity?

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
package com.litvy.litvysales.data.local.dao.purchases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.purchases.PurchaseItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseItemDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(purchaseItem: PurchaseItemEntity): Long

    @Query("SELECT * FROM purchaseItem WHERE id = :id")
    suspend fun getById(id: Int): PurchaseItemEntity?

    @Query("SELECT * FROM purchaseItem WHERE purchaseId = :purchaseId")
    fun getByPurchase(purchaseId: Int): Flow<List<PurchaseItemEntity>>

    @Query("SELECT * FROM purchaseItem WHERE productId = :productId")
    fun getByProduct(productId: Int): Flow<List<PurchaseItemEntity?>>

    @Query("SELECT * FROM purchaseItem ORDER BY id DESC")
    fun getAll(): Flow<List<PurchaseItemEntity?>>
}

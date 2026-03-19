package com.litvy.litvysales.data.local.dao.purchases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.litvy.litvysales.data.local.entity.purchases.PurchaseEntity
import com.litvy.litvysales.data.local.entity.purchases.PurchaseItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(purchase: PurchaseEntity): Long

    @Transaction
    suspend fun insertPurchaseWithItems(
        purchase: PurchaseEntity,
        items: List<PurchaseItemEntity>
    ): Long {
        val purchaseId = insert(purchase)

        val itemsWithPurchaseId = items.map {
            it.copy(purchaseId = purchaseId.toInt())
        }

        insertItems(itemsWithPurchaseId)
        return purchaseId
    }

    @Insert
    suspend fun insertItems(items: List<PurchaseItemEntity>)

    @Update
    suspend fun update(purchase: PurchaseEntity)

    @Query("DELETE FROM purchase WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM purchase WHERE id = :id")
    suspend fun getById(id: Int): PurchaseEntity?

    @RawQuery(observedEntities = [PurchaseEntity::class])
    fun getByFilter(query: SupportSQLiteQuery): Flow<List<PurchaseEntity>>
}

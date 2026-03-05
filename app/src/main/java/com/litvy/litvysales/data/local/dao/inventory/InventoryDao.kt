package com.litvy.litvysales.data.local.dao.inventory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.inventory.InventoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(inventory: InventoryEntity)

    @Query("SELECT * FROM inventory WHERE productId = :productId")
    fun getByProductId(productId: Int): Flow<InventoryEntity?>

    @Query("SELECT * FROM inventory")
    fun getAllProductsInventory(): Flow<List<InventoryEntity>>

}
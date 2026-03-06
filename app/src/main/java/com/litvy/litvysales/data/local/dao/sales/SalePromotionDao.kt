package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.litvy.litvysales.data.local.entity.sales.SalePromotionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SalePromotionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(salePromotion: SalePromotionEntity)

    @Query("SELECT * FROM sale_promotion WHERE id = :id")
    suspend fun getById(id: Int): SalePromotionEntity?

    @Query("SELECT * FROM sale_promotion WHERE saleId = :saleId")
    fun getBySale(saleId: Int): Flow<List<SalePromotionEntity?>>

    @Query("SELECT * FROM sale_promotion WHERE promotionId = :promotionId")
    fun getByPromotion(promotionId: Int): Flow<List<SalePromotionEntity?>>

}
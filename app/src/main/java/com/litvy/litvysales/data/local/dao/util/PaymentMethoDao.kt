package com.litvy.litvysales.data.local.dao.util

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.util.PaymentMethodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentMethoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(paymentMethod: PaymentMethodEntity)

    @Update
    suspend fun update(paymentMethod: PaymentMethodEntity)

    @Query("SELECT * FROM paymentMethod WHERE id = :id")
    suspend fun getById(id: Int): PaymentMethodEntity?

    @Query("SELECT * FROM paymentMethod")
    fun getAll(): Flow<List<PaymentMethodEntity?>>
}
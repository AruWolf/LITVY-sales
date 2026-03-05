package com.litvy.litvysales.data.local.dao.sales

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.litvy.litvysales.data.local.entity.sales.CustomerEntity
import kotlinx.coroutines.flow.Flow

interface CustomerDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(customer: CustomerEntity)

    @Update
    suspend fun update(customer: CustomerEntity)

    @Query("SELECT * FROM customer WHERE id = :id")
    suspend fun getById(id: Int): CustomerEntity?

    @Query("SELECT * FROM customer WHERE name = :name AND lastname = :lastName")
    suspend fun getByFullName(name: String, lastName: String): CustomerEntity?

    @Query("SELECT * FROM customer WHERE cuit = :cuit")
    suspend fun getByCuit(cuit: String): CustomerEntity?

    @Query("SELECT * FROM customer ORDER BY lastname ASC")
    fun getAll(): Flow<List<CustomerEntity?>>

    @Query("SELECT * FROM customer WHERE address = :address")
    suspend fun getByAddress(address: String): CustomerEntity?

}
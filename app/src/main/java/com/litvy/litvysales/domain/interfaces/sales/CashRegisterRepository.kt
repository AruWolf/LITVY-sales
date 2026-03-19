package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.filter.sales.CashRegisterFilter
import com.litvy.litvysales.domain.model.sales.CashRegister
import kotlinx.coroutines.flow.Flow

interface CashRegisterRepository {

    suspend fun create(cashRegister: CashRegister): Long

    suspend fun toggleActive(cashRegisterId: Int)
    fun getAll(): Flow<List<CashRegister?>>

    fun getAllActives(): Flow<List<CashRegister?>>

    suspend fun getById(cashRegisterId: Int): CashRegister?

    fun getCashRegisters(filter: CashRegisterFilter): Flow<List<CashRegister>>
}
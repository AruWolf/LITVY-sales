package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.filter.sales.CashMovementFilter
import com.litvy.litvysales.domain.model.sales.CashMovement
import kotlinx.coroutines.flow.Flow

interface CashMovementRepository {

    suspend fun create(movement: CashMovement): Long

    suspend fun getById(id: Int): CashMovement?

    fun getCashMovements(filter: CashMovementFilter): Flow<List<CashMovement>>

}
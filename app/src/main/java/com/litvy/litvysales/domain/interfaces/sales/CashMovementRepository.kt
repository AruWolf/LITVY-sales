package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.model.sales.CashMovement
import kotlinx.coroutines.flow.Flow

interface CashMovementRepository {

    suspend fun create(movement: CashMovement): Long

    suspend fun getById(id: Int): CashMovement?

    suspend fun getBySession(sessionId: Int): Flow<List<CashMovement?>>

    suspend fun getByType(type: String): Flow<List<CashMovement?>>

    suspend fun getByCreationDate(createdAt: Long): Flow<List<CashMovement?>>

    suspend fun getByUser(userId: Int): Flow<List<CashMovement?>>

}
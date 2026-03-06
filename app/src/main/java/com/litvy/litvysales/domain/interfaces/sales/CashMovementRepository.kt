package com.litvy.litvysales.domain.interfaces.sales

import com.litvy.litvysales.domain.model.sales.CashMovement

interface CashMovementRepository {

    suspend fun create(movement: CashMovement): Long

    suspend fun getBySession(sessionId: Int): List<CashMovement>

}
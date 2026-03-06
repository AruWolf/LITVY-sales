package com.litvy.litvysales.domain.useCase.purchases

import com.litvy.litvysales.domain.interfaces.purchases.PurchaseRepository
import com.litvy.litvysales.domain.interfaces.inventory.StockBatchRepository
import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.inventory.StockBatch
import com.litvy.litvysales.domain.model.inventory.StockMovement
import com.litvy.litvysales.domain.model.enums.StockMovementType
import com.litvy.litvysales.domain.model.purchases.Purchase

class RegisterPurchaseUseCase(

    private val purchaseRepository: PurchaseRepository,
    private val stockBatchRepository: StockBatchRepository,
    private val stockMovementRepository: StockMovementRepository

) {

    suspend operator fun invoke(
        purchase: Purchase,
        batches: List<StockBatch>
    ) {

        if (batches.isEmpty()) {
            throw IllegalArgumentException("Purchase must contain at least one batch")
        }

        // 1️⃣ Registrar compra
        val purchaseId = purchaseRepository.create(purchase)

        // 2️⃣ Crear lotes y movimientos
        batches.forEach { batch ->

            val batchToInsert = batch.copy(
                purchaseItemId = purchaseId.toInt()
            )

            stockBatchRepository.loadBatch(batchToInsert)

            val movement = StockMovement(
                id = 0,
                productId = batch.productId,
                batchId = batch.id,
                type = StockMovementType.PURCHASE,
                quantity = batch.quantity,
                createdAt = System.currentTimeMillis(),
                referenceId = purchaseId.toInt(),
                referenceType = "PURCHASE",
                createdBy = purchase.createdBy
            )

            stockMovementRepository.generateMovement(movement)

        }

    }

}
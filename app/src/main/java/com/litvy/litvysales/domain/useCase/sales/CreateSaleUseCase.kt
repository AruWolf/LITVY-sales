package com.litvy.litvysales.domain.useCase.sales

import com.litvy.litvysales.domain.interfaces.sales.SaleRepository
import com.litvy.litvysales.domain.interfaces.inventory.InventoryRepository
import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.sales.*
import com.litvy.litvysales.domain.model.inventory.StockMovement
import com.litvy.litvysales.domain.model.enums.StockMovementType
import kotlinx.coroutines.flow.first

class CreateSaleUseCase(
    private val saleRepository: SaleRepository,
    private val inventoryRepository: InventoryRepository,
    private val stockMovementRepository: StockMovementRepository
) {

    suspend operator fun invoke(
        sale: Sale,
        items: List<SaleItem>,
        payments: List<SalePayment>
    ): Long {

        if (items.isEmpty()) {
            throw IllegalArgumentException("Sale must contain at least one item")
        }

        if (payments.isEmpty()) {
            throw IllegalArgumentException("Sale must contain at least one payment")
        }

        // Validar stock
        items.forEach { item ->

            val inventory = inventoryRepository
                .getInventoryByProduct(item.productId)
                .first()

            val stock = inventory?.stock ?: 0.0

            if (stock < item.quantity) {
                throw IllegalStateException(
                    "Insufficient stock for product ${item.productId}"
                )
            }
        }

        // Registrar venta
        val saleId = saleRepository.create(sale)

        // Generar movimientos de stock
        items.forEach { item ->

            val movement = StockMovement(
                id = 0,
                productId = item.productId,
                batchId = null,
                type = StockMovementType.SALE,
                quantity = item.quantity,
                createdAt = System.currentTimeMillis(),
                referenceId = saleId.toInt(),
                referenceType = "SALE",
                createdBy = sale.sellerId
            )

            stockMovementRepository.generateMovement(movement)
        }

        return saleId
    }
}
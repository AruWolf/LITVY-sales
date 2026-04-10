package com.litvy.litvysales.domain.useCase.sales

import com.litvy.litvysales.domain.interfaces.sales.SaleRepository
import com.litvy.litvysales.domain.interfaces.inventory.InventoryRepository
import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.interfaces.sales.SaleItemRepository
import com.litvy.litvysales.domain.interfaces.sales.SalePaymentRepository
import com.litvy.litvysales.domain.model.sales.*
import com.litvy.litvysales.domain.model.inventory.StockMovement
import com.litvy.litvysales.domain.model.enums.StockMovementType
import com.litvy.litvysales.domain.validation.ValidationResult
import kotlinx.coroutines.flow.first

class CreateSaleUseCase(
    private val saleRepository: SaleRepository,
    private val inventoryRepository: InventoryRepository,
    private val stockMovementRepository: StockMovementRepository,
    private val saleItemRepository: SaleItemRepository,
    private val salePaymentRepository: SalePaymentRepository,
    private val validateSaleUseCase: ValidateSaleUseCase
) {

    suspend operator fun invoke(
        sale: Sale,
        items: List<SaleItem>,
        payments: List<SalePayment>
    ): Long {

        // Validaciones de negocio (UI-friendly)
        val validation = validateSaleUseCase(sale, items, payments)

        if (validation is ValidationResult.Failure) {
            throw IllegalStateException(
                validation.errors.joinToString { it.message }
            )
        }

        // Validar stock
        /* TODO: Implementar en versión que incluya control de stock y la opción de desactivarlo
        items.forEach { item ->
            val inventory = inventoryRepository
                .getInventoryByProduct(item.productId)
                .first()

            val stock = inventory?.stock ?: 0.0

            if (stock < item.quantity) {
                throw IllegalStateException(
                    "Stock insuficiente para el producto ${item.productId}"
                )
            }
        }*/

        // Crear venta
        val saleId = saleRepository.create(sale)

        // Guardar items
        items.forEach { item ->
            saleItemRepository.create(
                item.copy(saleId = saleId.toInt())
            )
        }

        // Guardar pagos
        payments.forEach { payment ->
            salePaymentRepository.create(
                payment.copy(saleId = saleId.toInt())
            )
        }

        // Movimientos de stock
        /*
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
        }*/

        return saleId
    }
}
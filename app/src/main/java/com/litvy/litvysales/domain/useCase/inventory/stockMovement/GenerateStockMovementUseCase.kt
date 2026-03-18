package com.litvy.litvysales.domain.useCase.inventory.stockMovement

import androidx.core.content.contentValuesOf
import com.litvy.litvysales.domain.interfaces.catalog.ProductRepository
import com.litvy.litvysales.domain.interfaces.inventory.InventoryRepository
import com.litvy.litvysales.domain.interfaces.inventory.StockBatchRepository
import com.litvy.litvysales.domain.interfaces.inventory.StockMovementRepository
import com.litvy.litvysales.domain.model.enums.StockMovementType
import com.litvy.litvysales.domain.model.inventory.StockMovement
import com.litvy.litvysales.domain.validation.CommonValidators
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult
import kotlinx.coroutines.flow.first

class GenerateStockMovementUseCase(
    private val stockMovementRepository: StockMovementRepository,
    private val productRepository: ProductRepository,
    private val batchRepository: StockBatchRepository,
    private val inventoryRepository: InventoryRepository
) {
    suspend operator fun invoke(movement: StockMovement): ValidationResult {

        val validator = ValidationBuilder()

        validator.check(
            productRepository.existsById(movement.productId),
            "product",
            "Producto inexistente")

        validator.check(
            batchRepository.existsById(movement.batchId),
            "batch",
            "Lote inexistente"
        )

        movement.batchId?.let {
            validator.check(
                batchRepository.belongsToProduct(it, movement.productId),
                "batch",
                "El lote no pertenece al producto"
            )
        }

        validator.check(
            movement.quantity > 0,
            "quantity",
            "Ingrese una cantidad valida"
        )

        /*
        if (movement.type == StockMovementType.SALE || movement.type == StockMovementType.LOSS) {
            val inventory = inventoryRepository.getInventoryByProduct(movement.productId).first()

            if (inventory == null) {
                validator.check(false, "product", "No existe inventario para el producto")
            } else {
                validator.check(
                    inventory.stock >= movement.quantity,
                    "quantity",
                    "Stock insuficiente"
                )
            }
        }*/ // TODO: Desarrollar una validación que tambien tenga en cuenta los lotes.
        // Posible error vendo 10 productos de un lote A que tiene 5, mientras tengo otro lote B que no se ve afectado.

        // TODO: Agregar validación para usuario existente y con rol apto

        val result = validator.build()

        if(result is ValidationResult.Failure && result.errors.isNotEmpty()) return result

        val now = System.currentTimeMillis()

        stockMovementRepository.generateMovement(
            StockMovement(
                id = null,
                productId = movement.productId,
                batchId = movement.batchId,
                type = movement.type,
                quantity = movement.quantity,
                createdAt = now,
                referenceId = movement.referenceId,
                referenceType = movement.referenceType,
                createdBy = movement.createdBy
        ))

        return result
    }

}
package com.litvy.litvysales.domain.useCase.sales

import com.litvy.litvysales.domain.useCase.catalog.product.GetProductByIdUseCase
import com.litvy.litvysales.domain.interfaces.sales.SaleItemRepository
import com.litvy.litvysales.domain.interfaces.sales.SalePaymentRepository
import com.litvy.litvysales.domain.interfaces.sales.SaleRepository
import com.litvy.litvysales.domain.model.sales.SaleDetail
import com.litvy.litvysales.domain.model.sales.SaleDetailItem
import kotlinx.coroutines.flow.first

class GetSaleDetailUseCase(
    private val saleRepository: SaleRepository,
    private val saleItemRepository: SaleItemRepository,
    private val salePaymentRepository: SalePaymentRepository,
    private val getProductByIdUseCase: GetProductByIdUseCase
) {

    suspend operator fun invoke(saleId: Int): SaleDetail? {
        val sale = saleRepository.getById(saleId) ?: return null
        val items = saleItemRepository.getBySale(saleId).first()
        val payments = salePaymentRepository.getBySale(saleId).first()
        return SaleDetail(
            sale = sale,
            items = items.map { item ->
                SaleDetailItem(
                    id = item.id,
                    productId = item.productId,
                    productName = getProductByIdUseCase(item.productId)?.name ?: "Producto desconocido",
                    quantity = item.quantity,
                    totalInCents = item.totalInCents
                )
            },
            payments = payments
        )
    }
}

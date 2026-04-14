package com.litvy.litvysales.domain.useCase.sales

import com.litvy.litvysales.domain.model.sales.CashSessionDefaults
import com.litvy.litvysales.domain.model.sales.Sale
import com.litvy.litvysales.domain.model.sales.SaleItem
import com.litvy.litvysales.domain.model.sales.SalePayment
import com.litvy.litvysales.domain.useCase.sales.cashSession.GetOrCreateOpenCashSessionUseCase

class CreateSaleWithCashSessionUseCase(
    private val getOrCreateOpenCashSessionUseCase: GetOrCreateOpenCashSessionUseCase,
    private val createSaleUseCase: CreateSaleUseCase
) {

    suspend operator fun invoke(
        sale: Sale,
        items: List<SaleItem>,
        payments: List<SalePayment>,
        registerId: Int = CashSessionDefaults.DEFAULT_CASH_REGISTER_ID,
        sellerId: Int = CashSessionDefaults.DEFAULT_USER_ID
    ): Long {
        val session = getOrCreateOpenCashSessionUseCase(
            registerId = registerId,
            openedBy = sellerId
        )

        return createSaleUseCase(
            sale = sale.copy(
                cashSessionId = session.id,
                sellerId = sellerId
            ),
            items = items,
            payments = payments
        )
    }
}

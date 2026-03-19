package com.litvy.litvysales.domain.useCase.purchases

import com.litvy.litvysales.domain.interfaces.purchases.InvoiceTypeRepository
import com.litvy.litvysales.domain.model.purchases.InvoiceType
import kotlinx.coroutines.flow.first

class GetInvoiceTypesUseCase(
    private val repository: InvoiceTypeRepository
) {

    suspend operator fun invoke(): List<InvoiceType> {
        return repository.getAll().first().mapNotNull { it }
    }
}

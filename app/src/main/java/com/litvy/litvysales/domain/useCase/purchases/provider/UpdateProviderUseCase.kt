package com.litvy.litvysales.domain.useCase.purchases.provider

import com.litvy.litvysales.domain.filter.purchases.ProviderFilter
import com.litvy.litvysales.domain.interfaces.purchases.ProviderRepository
import com.litvy.litvysales.domain.model.purchases.Provider
import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays
import com.litvy.litvysales.domain.validation.ValidationBuilder
import com.litvy.litvysales.domain.validation.ValidationResult
import kotlinx.coroutines.flow.first

class UpdateProviderUseCase(
    private val repository: ProviderRepository
) {

    suspend operator fun invoke(
        provider: Provider,
        visitDays: Collection<Int>
    ): ValidationResult {
        val providerId = provider.id
        val validator = ValidationBuilder()

        validator.check(
            providerId != null && providerId > 0,
            "id",
            "El proveedor debe existir para poder actualizarse"
        )

        val normalizedProvider = provider.normalizeForPersistence(id = providerId)
        val normalizedVisitDays = visitDays.normalizeVisitDays()

        validateProviderData(
            validator = validator,
            provider = normalizedProvider,
            visitDays = normalizedVisitDays
        )

        val existing = repository.getProviders(
            ProviderFilter(name = normalizedProvider.name)
        ).first()
        validator.check(
            existing.none {
                it.id != providerId &&
                    it.name.equals(normalizedProvider.name, ignoreCase = true)
            },
            "name",
            "Ya existe un proveedor con ese nombre"
        )

        val result = validator.build()
        if (result is ValidationResult.Failure && result.errors.isNotEmpty()) {
            return result
        }

        repository.update(
            ProviderWithVisitDays(
                provider = normalizedProvider,
                visitDays = normalizedVisitDays
            )
        )

        return ValidationResult.Success
    }
}

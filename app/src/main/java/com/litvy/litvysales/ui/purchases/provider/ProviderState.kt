package com.litvy.litvysales.ui.purchases.provider

import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays

data class ProviderState(
    val providers: List<ProviderWithVisitDays> = emptyList(),
    val search: String = "",
    val selectedProvider: ProviderWithVisitDays? = null,
    val isEditing: Boolean = false,
    val isCreating: Boolean = false,
    val name: String = "",
    val cuit: String = "",
    val phone: String = "",
    val address: String = "",
    val email: String = "",
    val visitDays: Set<Int> = emptySet(),
    val errors: Map<String, String> = emptyMap(),
    val feedbackMessage: String? = null
) {
    val filteredProviders: List<ProviderWithVisitDays>
        get() = if (search.isBlank()) {
            providers
        } else {
            providers.filter {
                it.provider.name.contains(search, ignoreCase = true) ||
                    it.provider.cuit?.contains(search, ignoreCase = true) == true
            }
        }
}

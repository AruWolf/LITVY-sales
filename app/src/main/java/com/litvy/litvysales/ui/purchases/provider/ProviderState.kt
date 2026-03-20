package com.litvy.litvysales.ui.purchases.provider

import com.litvy.litvysales.domain.filter.common.QuerySortDirection
import com.litvy.litvysales.domain.filter.purchases.ProviderSortBy
import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays

data class ProviderState(

    // ESTADOS DE CONSULTA
    val providers: List<ProviderWithVisitDays> = emptyList(),
    val search: String = "",
    val selectedProvider: ProviderWithVisitDays? = null,
    val sortBy: ProviderSortBy = ProviderSortBy.NAME,
    val sortDirection: QuerySortDirection = QuerySortDirection.ASC,

    // ESTADOS DE EVENTOS
    val isEditing: Boolean = false,
    val isCreating: Boolean = false,
    val showDialog: Boolean = false,

    // NAVEGACION
    val navigateBack: Boolean = false,

    // DATOS DE FORMULARIO
    val name: String = "",
    val cuit: String = "",
    val phone: String = "",
    val address: String = "",
    val email: String = "",
    val visitDays: Set<Int> = emptySet(),

    // ESTADOS DE VALIDACIÓN DE ERRORES/ADVERTENCIAS
    val errors: Map<String, String> = emptyMap(),
    val feedbackMessage: String? = null,
)



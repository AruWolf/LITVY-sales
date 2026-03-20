package com.litvy.litvysales.ui.purchases.provider

import com.litvy.litvysales.domain.filter.purchases.ProviderSortBy
import com.litvy.litvysales.domain.model.purchases.Provider

sealed class ProviderEvent {

    // LIST
    data class OnSearchChange(val value: String): ProviderEvent()
    data class OnProviderSelected(val provider: Provider): ProviderEvent()
    object OnAddNew: ProviderEvent()
    object OnApplyFilters : ProviderEvent()
    object OnClearFilters : ProviderEvent()
    data class OnSortChange(val sortBy: ProviderSortBy) : ProviderEvent()

    // MODOS
    object OnEditClick: ProviderEvent()
    object OnCancelEdit: ProviderEvent()
    object OnSave: ProviderEvent()

    // FORM
    data class OnNameChange(val value: String): ProviderEvent()
    data class OnCuitChange(val value: String): ProviderEvent()
    data class OnPhoneChange(val value: String): ProviderEvent()
    data class OnAddressChange(val value: String): ProviderEvent()
    data class OnEmailChange(val value: String): ProviderEvent()
    data class OnVisitDayToggle(val day: Int): ProviderEvent()

    // DIALOG
    object OnDismissDialog : ProviderEvent()

    // NAVEGACION
    object OnBack: ProviderEvent()

}
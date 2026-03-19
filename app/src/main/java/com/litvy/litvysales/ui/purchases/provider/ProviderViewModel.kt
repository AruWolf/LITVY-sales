package com.litvy.litvysales.ui.purchases.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.domain.model.purchases.Provider
import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays
import com.litvy.litvysales.domain.useCase.purchases.provider.CreateProviderUseCase
import com.litvy.litvysales.domain.useCase.purchases.provider.GetProvidersWithVisitDaysUseCase
import com.litvy.litvysales.domain.useCase.purchases.provider.UpdateProviderUseCase
import com.litvy.litvysales.domain.validation.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProviderViewModel(
    private val getProvidersWithVisitDaysUseCase: GetProvidersWithVisitDaysUseCase,
    private val createProviderUseCase: CreateProviderUseCase,
    private val updateProviderUseCase: UpdateProviderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProviderState())
    val state: StateFlow<ProviderState> = _state.asStateFlow()

    init {
        observeProviders()
    }

    fun onEvent(event: ProviderEvent) {
        when (event) {
            is ProviderEvent.OnSearchChange -> _state.update { it.copy(search = event.value) }
            is ProviderEvent.OnProviderSelected -> selectProvider(event.provider.id)
            ProviderEvent.OnAddNew -> startCreate()
            ProviderEvent.OnEditClick -> _state.update { it.copy(isEditing = true, isCreating = false, feedbackMessage = null) }
            ProviderEvent.OnCancelEdit -> cancelEdit()
            ProviderEvent.OnSave -> save()
            is ProviderEvent.OnNameChange -> updateForm(name = event.value)
            is ProviderEvent.OnCuitChange -> updateForm(cuit = event.value)
            is ProviderEvent.OnPhoneChange -> updateForm(phone = event.value)
            is ProviderEvent.OnAddressChange -> updateForm(address = event.value)
            is ProviderEvent.OnEmailChange -> updateForm(email = event.value)
            is ProviderEvent.OnVisitDayToggle -> toggleVisitDay(event.day)
        }
    }

    private fun observeProviders() {
        viewModelScope.launch {
            getProvidersWithVisitDaysUseCase().collect { providers ->
                _state.update { current ->
                    val selectedId = current.selectedProvider?.provider?.id
                    val selected = providers.firstOrNull { it.provider.id == selectedId } ?: providers.firstOrNull()
                    current.copy(
                        providers = providers,
                        selectedProvider = if (current.isCreating) null else selected
                    ).hydrateFormFromSelectionIfNeeded()
                }
            }
        }
    }

    private fun selectProvider(providerId: Int?) {
        val selected = _state.value.providers.firstOrNull { it.provider.id == providerId }
        _state.update {
            it.copy(
                selectedProvider = selected,
                isEditing = false,
                isCreating = false,
                errors = emptyMap(),
                feedbackMessage = null
            ).hydrateFormFromSelection()
        }
    }

    private fun startCreate() {
        _state.update {
            it.copy(
                selectedProvider = null,
                isCreating = true,
                isEditing = true,
                name = "",
                cuit = "",
                phone = "",
                address = "",
                email = "",
                visitDays = emptySet(),
                errors = emptyMap(),
                feedbackMessage = null
            )
        }
    }

    private fun cancelEdit() {
        _state.update {
            it.copy(
                isEditing = false,
                isCreating = false,
                errors = emptyMap(),
                feedbackMessage = null
            ).hydrateFormFromSelection()
        }
    }

    private fun updateForm(
        name: String? = null,
        cuit: String? = null,
        phone: String? = null,
        address: String? = null,
        email: String? = null
    ) {
        _state.update {
            it.copy(
                name = name ?: it.name,
                cuit = cuit ?: it.cuit,
                phone = phone ?: it.phone,
                address = address ?: it.address,
                email = email ?: it.email,
                errors = it.errors - setOf("name", "cuit", "telephoneNumber", "email")
            )
        }
    }

    private fun toggleVisitDay(day: Int) {
        _state.update {
            val next = if (day in it.visitDays) it.visitDays - day else it.visitDays + day
            it.copy(visitDays = next)
        }
    }

    private fun save() {
        val current = _state.value
        val provider = Provider(
            id = current.selectedProvider?.provider?.id,
            name = current.name,
            cuit = current.cuit.ifBlank { null },
            telephoneNumber = current.phone.ifBlank { null },
            address = current.address.ifBlank { null },
            email = current.email.ifBlank { null }
        )

        viewModelScope.launch {
            val result = if (current.isCreating) {
                createProviderUseCase(provider, current.visitDays)
            } else {
                updateProviderUseCase(provider, current.visitDays)
            }

            when (result) {
                ValidationResult.Success -> {
                    _state.update {
                        it.copy(
                            isEditing = false,
                            isCreating = false,
                            errors = emptyMap(),
                            feedbackMessage = if (current.isCreating) {
                                "Proveedor creado correctamente."
                            } else {
                                "Proveedor actualizado correctamente."
                            }
                        )
                    }
                }

                is ValidationResult.Failure -> {
                    _state.update {
                        it.copy(
                            errors = result.errors.associate { issue -> issue.field to issue.message },
                            feedbackMessage = "Revisa los datos del proveedor."
                        )
                    }
                }
            }
        }
    }

    private fun ProviderState.hydrateFormFromSelection(): ProviderState {
        val provider = selectedProvider?.provider
        return copy(
            name = provider?.name.orEmpty(),
            cuit = provider?.cuit.orEmpty(),
            phone = provider?.telephoneNumber.orEmpty(),
            address = provider?.address.orEmpty(),
            email = provider?.email.orEmpty(),
            visitDays = selectedProvider?.visitDays ?: emptySet()
        )
    }

    private fun ProviderState.hydrateFormFromSelectionIfNeeded(): ProviderState {
        return if (isEditing) this else hydrateFormFromSelection()
    }
}

class ProviderViewModelFactory(
    private val getProvidersWithVisitDaysUseCase: GetProvidersWithVisitDaysUseCase,
    private val createProviderUseCase: CreateProviderUseCase,
    private val updateProviderUseCase: UpdateProviderUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProviderViewModel(
            getProvidersWithVisitDaysUseCase,
            createProviderUseCase,
            updateProviderUseCase
        ) as T
    }
}

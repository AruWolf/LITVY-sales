package com.litvy.litvysales.ui.purchases.provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.domain.filter.common.QuerySortDirection
import com.litvy.litvysales.domain.filter.purchases.ProviderFilter
import com.litvy.litvysales.domain.model.purchases.Provider
import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays
import com.litvy.litvysales.domain.useCase.purchases.provider.CreateProviderUseCase
import com.litvy.litvysales.domain.useCase.purchases.provider.GetProviderUseCase
import com.litvy.litvysales.domain.useCase.purchases.provider.GetProvidersWithVisitDaysUseCase
import com.litvy.litvysales.domain.useCase.purchases.provider.UpdateProviderUseCase
import com.litvy.litvysales.domain.validation.ValidationResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProviderViewModel(
    private val getProvidersWithVisitDaysUseCase: GetProvidersWithVisitDaysUseCase,
    private val createProviderUseCase: CreateProviderUseCase,
    private val updateProviderUseCase: UpdateProviderUseCase,
    private val getProviderUseCase: GetProviderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProviderState())
    val state: StateFlow<ProviderState> = _state.asStateFlow()

    init {
        applyFilters()
    }

    fun onEvent(event: ProviderEvent) {
        when (event) {
            is ProviderEvent.OnSearchChange -> {
                _state.update { it.copy(search = event.value) }
            }
            ProviderEvent.OnApplyFilters -> applyFilters()
            ProviderEvent.OnClearFilters -> clearFilters()
            is ProviderEvent.OnSortChange -> {
                _state.update {

                    val isSameField = it.sortBy == event.sortBy

                    it.copy(
                        sortBy = event.sortBy,
                        sortDirection = if (isSameField) {
                            if (it.sortDirection == QuerySortDirection.ASC)
                                QuerySortDirection.DESC
                            else QuerySortDirection.ASC
                        } else {
                            QuerySortDirection.ASC
                        }
                    )
                }

                applyFilters()
            }
            is ProviderEvent.OnProviderSelected -> {
                selectProvider(event.provider.id)
                _state.update { it.copy(showDialog = true) }
            }
            ProviderEvent.OnAddNew -> {
                startCreate()
                _state.update { it.copy(showDialog = true) }
            }
            ProviderEvent.OnEditClick -> _state.update { it.copy(isEditing = true, isCreating = false, feedbackMessage = null) }
            ProviderEvent.OnCancelEdit -> cancelEdit()
            ProviderEvent.OnSave -> save()
            ProviderEvent.OnDismissDialog -> {
                _state.update {
                    it.copy(
                        showDialog = false,
                        isEditing = false,
                        isCreating = false,
                        errors = emptyMap(),
                        feedbackMessage = null
                    )
                }
            }
            ProviderEvent.OnBack -> onBackNavigation()

            is ProviderEvent.OnNameChange -> updateForm(name = event.value)
            is ProviderEvent.OnCuitChange -> updateForm(cuit = event.value)
            is ProviderEvent.OnPhoneChange -> updateForm(phone = event.value)
            is ProviderEvent.OnAddressChange -> updateForm(address = event.value)
            is ProviderEvent.OnEmailChange -> updateForm(email = event.value)
            is ProviderEvent.OnVisitDayToggle -> toggleVisitDay(event.day)
        }
    }

    // Maneja el evento de navegación para regresar a la pantalla previa
    private fun onBackNavigation() {
        _state.update {
            it.copy(navigateBack = true)
        }
    }

    fun onNavigationHandled() {
        _state.update {
            it.copy(navigateBack = false)
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

    // Maneja el evento de selección de un proveedor de la lista, para generar el dialog de visualización de datos.
    private fun selectProvider(providerId: Int?) {
        val selected = _state.value.providers.firstOrNull { it.provider.id == providerId }
        _state.update {
            it.copy( // Genera el dialog mediante los parametros
                selectedProvider = selected,
                isEditing = false,
                isCreating = false,
                errors = emptyMap(),
                feedbackMessage = null
            ).hydrateFormFromSelection()
        }
    }

    // Maneja el evento de creación de un proveedor, para generar el dialog de creación
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

    // Maneja el cierre del modo edición del dialog
    private fun cancelEdit() {
        _state.update {

            if (it.isCreating) {
                it.copy(
                    showDialog = false,
                    isEditing = false,
                    isCreating = false,
                    errors = emptyMap(),
                    feedbackMessage = null
                )
            }
            else {
                it.copy(
                    isEditing = false,
                    isCreating = false,
                    errors = emptyMap(),
                    feedbackMessage = null
                ).hydrateFormFromSelection()
            }
        }
    }

    // Maneja el formulario de edición y los datos editados
    private fun updateForm(
        name: String? = null,
        cuit: String? = null,
        phone: String? = null,
        address: String? = null,
        email: String? = null
    ) {
        _state.update {
            it.copy(
                name = name?.replaceFirstChar { it.uppercase() } ?: it.name,
                cuit = cuit?.filter { it.isDigit() }?.take(11) ?: it.cuit,
                phone = phone?.filter { it.isDigit() }?.take(11) ?: it.phone,
                address = address ?: it.address,
                email = email?.trim() ?: it.email,
                errors = emptyMap()
            )
        }
    }

    // Maneja la polarización de los dias seleccionados para la visita del proveedor
    private fun toggleVisitDay(day: Int) {
        _state.update {
            val next = if (day in it.visitDays) it.visitDays - day else it.visitDays + day
            it.copy(
                visitDays = next,
                errors = it.errors - "visitDays"
            )
        }
    }

    // Maneja el guardado de los datos cargados en formulario
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

            // Manejo de validación de datos cargados
            when (result) {
                ValidationResult.Success -> { // Resultado exitoso
                    _state.update {
                        it.copy(
                            isEditing = false,
                            isCreating = false,
                            errors = emptyMap(),
                            feedbackMessage = if (current.isCreating) {
                                "Proveedor creado correctamente."
                            } else {
                                "Proveedor actualizado correctamente."
                            },
                            showDialog = false
                        )
                    }

                }
                // Resultado con errores
                is ValidationResult.Failure -> {
                    _state.update {
                        it.copy(
                            // Genera el mensaje correspondiente a los campos con errores
                            errors = result.errors.associate { issue -> issue.field to issue.message },
                            feedbackMessage = "Revisa los datos del proveedor."
                        )
                    }
                }
            }
        }
    }

    private var filterJob: Job? = null

    // Maneja los filtros aplicados para la busqueda de proveedores
    private fun applyFilters() {
        filterJob?.cancel()

        filterJob = viewModelScope.launch {

            val current = _state.value

            val filter = ProviderFilter(
                name = current.search,
                cuit = current.cuit,
                telephoneNumber = current.phone,
                address = current.address,
                email = current.email,
                sortBy = current.sortBy,
                sortDirection = current.sortDirection
            )

            getProviderUseCase(filter).collect { providers ->

                _state.update {
                    it.copy(
                        providers = providers.map { provider ->
                            ProviderWithVisitDays(provider, emptySet())
                        }
                    )
                }

                val selectedId = _state.value.selectedProvider?.provider?.id

                val newSelected = providers.firstOrNull { it.id == selectedId }

                _state.update {
                    it.copy(
                        providers = providers.map { ProviderWithVisitDays(it, emptySet()) },
                        selectedProvider = newSelected?.let { ProviderWithVisitDays(it, emptySet()) }
                    )
                }
            }
        }
    }

    // Maneja la limpieza de filtros
    private fun clearFilters() {
        _state.update {
            it.copy(
                search = "",
                cuit = "",
                phone = "",
                address = "",
                email = ""
            )
        }

        applyFilters()
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
    private val updateProviderUseCase: UpdateProviderUseCase,
    private val getProviderUseCase: GetProviderUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProviderViewModel(
            getProvidersWithVisitDaysUseCase,
            createProviderUseCase,
            updateProviderUseCase,
            getProviderUseCase
        ) as T
    }
}

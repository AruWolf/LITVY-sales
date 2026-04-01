package com.litvy.litvysales.ui.purchases.purchaseOrder.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.di.AppContainer
import com.litvy.litvysales.domain.filter.purchases.ProviderFilter
import com.litvy.litvysales.domain.model.enums.PurchaseOrderStatus
import com.litvy.litvysales.domain.model.purchases.PurchaseOrder
import com.litvy.litvysales.domain.model.purchases.PurchaseOrderItem
import com.litvy.litvysales.domain.validation.ValidationResult
import com.litvy.litvysales.ui.util.model.PurchaseOrderItemUi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class PurchaseOrderCreateViewModel(
    private val container: AppContainer
) : ViewModel() {

    private val _state = MutableStateFlow(PurchaseOrderCreateState())
    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    val state: StateFlow<PurchaseOrderCreateState> = _state

    init {
        loadData()

        viewModelScope.launch {
            val categories = container.getCategoriesUseCase()
            _state.update { it.copy(categories = categories) }
        }

    }

    private fun loadData() {
        viewModelScope.launch {
            val providers = container
                .getProviderUseCase(ProviderFilter())
                .first()

            val products = container.getActiveProductsWithBrandUseCase().first()

            _state.update {
                it.copy(
                    providers = providers,
                    products = products
                )
            }

            applyFilters()
        }
    }

    fun onEvent(event: PurchaseOrderCreateEvent) {
        when (event) {

            is PurchaseOrderCreateEvent.OnProviderSelected -> {
                _state.update {
                    it.copy(
                        selectedProviderId = event.providerId,
                ) }
            }

            is PurchaseOrderCreateEvent.OnDateChange -> {
                _state.update { it.copy(expectedDate = event.value) }
            }

            PurchaseOrderCreateEvent.OnAddItem -> {
                _state.update {
                    it.copy(
                        items = it.items + PurchaseOrderItemUi(
                            productId = 0,
                            productName = "",
                            quantity = 1.0,
                            suggestedUnitPriceInCents = null
                        )
                    )
                }
            }

            is PurchaseOrderCreateEvent.OnRemoveItem -> {
                _state.update {
                    it.copy(items = it.items.toMutableList().also {
                        it.removeAt(event.index)
                    })
                }
            }

            is PurchaseOrderCreateEvent.OnProductPicked -> {

                val index = _state.value.selectedItemIndex ?: return
                val product = _state.value.products
                    .firstOrNull { it.id == event.productId }

                _state.update {
                    it.copy(
                        items = it.items.toMutableList().also { list ->
                            list[index] = list[index].copy(
                                productId = event.productId,
                                productName = product?.name ?: "",
                                suggestedUnitPriceInCents = product?.purchasePriceInCents
                            )
                        },
                        isProductDialogOpen = false,
                        selectedItemIndex = null,
                        productSearch = "",
                        selectedCategoryId = null,
                        selectedSubCategoryId = null,
                        selectedBrandId = null,
                        subCategories = emptyList(),
                        brands = emptyList(),
                        filteredProducts = emptyList()
                    )
                }
            }

            is PurchaseOrderCreateEvent.OnQuantityChange -> {
                val qty = event.quantity.toDoubleOrNull() ?: 0.0

                _state.update {
                    it.copy(
                        items = it.items.toMutableList().also { list ->
                            list[event.index] = list[event.index].copy(
                                quantity = qty
                            )
                        }
                    )
                }
            }

            PurchaseOrderCreateEvent.OnSave -> saveOrder()

            is PurchaseOrderCreateEvent.OnOpenProductDialog -> {
                _state.update {
                    it.copy(
                        isProductDialogOpen = true,
                        selectedItemIndex = event.index
                    )
                }
            }


            is PurchaseOrderCreateEvent.OnCategorySelected -> {
                viewModelScope.launch {
                    val sub = container.getSubCategoriesByCategoryUseCase(event.id)

                    _state.update {
                        it.copy(
                            selectedCategoryId = event.id,
                            subCategories = sub,

                            selectedSubCategoryId = null,
                            selectedBrandId = null,
                            brands = emptyList(),
                        )
                    }

                    applyFilters()
                }
            }

            is PurchaseOrderCreateEvent.OnSubCategorySelected -> {
                viewModelScope.launch {
                    val brands = container.getBrandBySubCategoryUseCase(event.id)

                    _state.update {
                        it.copy(
                            selectedSubCategoryId = event.id,
                            brands = brands,
                            selectedBrandId = null,
                            productSearch = ""
                        )
                    }

                    applyFilters()
                }
            }

            is PurchaseOrderCreateEvent.OnBrandSelected -> {
                _state.update {
                    it.copy(selectedBrandId = event.id)
                }
                applyFilters()
            }

            is PurchaseOrderCreateEvent.OnProductSearchChange -> {
                _state.update { it.copy(productSearch = event.value) }
                applyFilters()
            }

            is PurchaseOrderCreateEvent.OnCloseProductDialog -> {
                _state.update {
                    it.copy(
                        isProductDialogOpen = false,
                        selectedItemIndex = null,
                        productSearch = "",
                        selectedCategoryId = null,
                        selectedSubCategoryId = null,
                        selectedBrandId = null,
                        subCategories = emptyList(),
                        brands = emptyList(),
                        filteredProducts = emptyList()
                    )
                }
            }

            else -> {}
        }
    }

    private fun saveOrder() {
        val current = _state.value

        val order = PurchaseOrder(
            id = 0,
            providerId = current.selectedProviderId ?: 0,
            status = PurchaseOrderStatus.PENDING,
            expectedDeliveryDate = null, // después parseás
            createdAt = System.currentTimeMillis(),
            createdBy = 1
        )

        val items = current.items.map {
            PurchaseOrderItem(
                id = 0,
                purchaseOrderId = 0,
                productId = it.productId,
                quantity = it.quantity
            )
        }

        viewModelScope.launch {

            val result = container.createPurchaseOrderUseCase(order, items)

            when (result) {

                is ValidationResult.Success -> {
                    _state.update {
                        it.copy(
                            items = emptyList(),
                            selectedProviderId = null
                        )
                    }

                    viewModelScope.launch {
                        _uiEvent.emit(UiEvent.Success)
                    }
                }

                is ValidationResult.Failure -> {
                    val errorsMap = result.errors.associate {
                        it.field to it.message
                    }

                    _state.update {
                        it.copy(
                            fieldErrors = errorsMap
                        )
                    }
                }
            }
        }
    }

    fun openProductDialog(index: Int) {
        _state.update {
            it.copy(
                isProductDialogOpen = true,
                selectedItemIndex = index
            )
        }
    }

    private fun applyFilters() {
        val current = _state.value

        val filtered = current.products.filter { product ->

            val matchesSearch =
                current.productSearch.isBlank() ||
                        product.name.contains(current.productSearch, true)

            if (current.productSearch.isNotBlank()
                && current.selectedCategoryId == null
            ) {
                return@filter matchesSearch
            }

            if (current.selectedSubCategoryId != null) {

                val allowedBrandIds = current.brands.mapNotNull { it.id }

                val matchSubCategory = product.brandId in allowedBrandIds

                val matchBrand = current.selectedBrandId == null ||
                        product.brandId == current.selectedBrandId

                return@filter matchSubCategory && matchBrand && matchesSearch
            }

            if (current.selectedCategoryId != null) {
                // no mostramos nada hasta subcategoría
                return@filter false
            }

            false
        }

        _state.update {
            it.copy(filteredProducts = filtered)
        }
    }

    class Factory(
        private val container: AppContainer
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PurchaseOrderCreateViewModel(container) as T
        }
    }
}
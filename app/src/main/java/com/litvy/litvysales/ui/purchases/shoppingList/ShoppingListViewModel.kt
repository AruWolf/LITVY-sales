package com.litvy.litvysales.ui.purchases.shoppingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.di.AppContainer
import com.litvy.litvysales.ui.purchases.shoppingList.components.ShoppingListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShoppingListViewModel(
    private val container: AppContainer
) : ViewModel() {
    private val _state = MutableStateFlow(ShoppingListUiState())
    val state: StateFlow<ShoppingListUiState> = _state

    init {
        observeData()
    }

    fun onEvent(event: ShoppingListEvent){
        when(event){
            is ShoppingListEvent.OnSearchChange -> {
                _state.update { it.copy(search = event.value) }
                applyFilters()
            }

            ShoppingListEvent.OnBack -> {

            }

            else -> {}
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                container.getActiveProductsWithBrandUseCase(),
                container.database.inventoryDao().getAllProductsInventory()
            ) { products, inventoryList ->

                val inventoryByProduct = inventoryList.associateBy { it.productId }

                products.mapNotNull { product ->
                    val productId = product.id
                    val inventory = inventoryByProduct[productId]

                    ShoppingListItem(
                        id = productId,
                        name = product.name,
                        currentStock = inventory?.stock ?: 0.0,
                        suggestedQuantity = 0.0,
                        provider = null,
                        brand = product.brandName // TODO: Llamar a los productos con su marca.
                    )
                }

            }.collect { items ->
                _state.update { it.copy(items = items) }
                applyFilters()
            }
        }
    }

    private fun applyFilters() {
        val current = _state.value

        val filtered = current.items.filter {
            current.search.isBlank() ||
                    it.name.contains(current.search, ignoreCase = true)
        }

        val grouped = filtered.groupBy { it.brand }

        _state.update {
            it.copy(groupedByBrand = grouped)
        }
    }

    class ShoppingListViewModelFactory(
        private val container: AppContainer
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ShoppingListViewModel(container) as T
        }
    }

}
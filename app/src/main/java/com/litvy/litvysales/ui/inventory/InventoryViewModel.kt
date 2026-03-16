package com.litvy.litvysales.ui.inventory

import androidx.lifecycle.ViewModel
import com.litvy.litvysales.domain.useCase.inventory.stockBatch.GetBatchByProductUseCase
import kotlinx.coroutines.flow.*
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.ui.inventory.model.ExpirationStatus
import com.litvy.litvysales.ui.inventory.model.StockBatchItem
import kotlinx.coroutines.launch

class InventoryViewModel(
    private val getBatchByProductUseCase: GetBatchByProductUseCase
): ViewModel() {
    private val _state = MutableStateFlow(InventoryState())
    val state: StateFlow<InventoryState> = _state

    fun onEvent(event: InventoryEvent){

        when(event){

            is InventoryEvent.SearchQueryChanged -> {

                _state.update { current ->
                    current.copy(
                        searchQuery = event.query
                    )
                }


            }

            is InventoryEvent.ProductExpanded -> {

                val productId = event.productId

                // Expandir productos
                _state.update { current ->
                    current.copy(
                        expandedProducts = current.expandedProducts + event.productId
                    )
                }

                // Carga los lotes según el producto seleccionado, solo si no esta cargado en el state.
                if (!_state.value.batchesByProduct.containsKey(productId)){
                    loadBatches(productId)
                }

            }

            is InventoryEvent.ProductCollapsed -> {
                val productId = event.productId

                // Ocultar detalle de productos
                _state.update { current ->
                    current.copy(
                        expandedProducts = current.expandedProducts - event.productId
                    )
                }
            }

            is InventoryEvent.RefreshInventory -> {
                // Recargar pantalla?
            }



            else -> {}
        }
    }

    private fun loadBatches(productId: Int) {

        viewModelScope.launch {

            // Caso de uso que trae los lotes según el producto seleccionado, recolectandolos bajo un mapeo,
            // y actualizando el state para mostrarlos en pantalla
            getBatchByProductUseCase(productId)
                .collect { batches ->

                    // Mapeo para mostrar los lotes
                    val batchItems = batches.map { batch ->
                        StockBatchItem(
                            id = batch.id ?: 0,
                            quantity = batch.quantity,
                            expirationDate = batch.expirationDate,
                            expirationStatus = ExpirationStatus.OK
                        )
                    }

                    // Actualización del state, para mostrar en pantalla los lotes, según el producto
                    _state.update { current ->
                        current.copy(
                            batchesByProduct =
                                current.batchesByProduct + (productId to batchItems)
                        )
                    }

                }

        }

    }
}
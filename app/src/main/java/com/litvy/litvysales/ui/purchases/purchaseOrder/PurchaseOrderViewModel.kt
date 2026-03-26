package com.litvy.litvysales.ui.purchases.purchaseOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.di.AppContainer
import com.litvy.litvysales.domain.model.enums.PurchaseOrderStatus
import com.litvy.litvysales.domain.model.purchases.Purchase
import com.litvy.litvysales.domain.model.purchases.PurchaseOrder
import com.litvy.litvysales.domain.model.purchases.PurchaseOrderItem
import com.litvy.litvysales.domain.validation.ValidationResult
import com.litvy.litvysales.ui.util.model.PurchaseOrderItemUi
import com.litvy.litvysales.ui.util.model.PurchaseOrderUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.collections.orEmpty

class PurchaseOrderViewModel(
    private val container: AppContainer
) : ViewModel() {

    private val _state = MutableStateFlow(PurchaseOrderState())
    val state: StateFlow<PurchaseOrderState> = _state

    private var domainOrders: List<PurchaseOrder> = emptyList()
    private var domainItems: Map<Int, List<PurchaseOrderItem>> = emptyMap()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("es", "AR"))
            val providers = container.getProvidersWithVisitDaysUseCase().first().associateBy { it.provider.id }
            val products = container.getActiveProductsUseCase().first().associateBy { it.id }

            container.getPurchaseOrdersUseCase().collect { orders ->

                domainOrders = orders

                val itemsMap = mutableMapOf<Int, List<PurchaseOrderItem>>()

                orders.forEach { order ->
                    itemsMap[order.id] =
                        container.getPurchaseOrderItemsUseCase(order.id).first()
                }

                domainItems = itemsMap

                val uiOrders = orders.map { order ->
                    PurchaseOrderUi(
                        id = order.id,
                        providerId = order.providerId,
                        providerName = providers[order.providerId]?.provider?.name
                            ?: "Proveedor #${order.providerId}",
                        status = order.status.name,
                        expectedDeliveryLabel = order.expectedDeliveryDate?.let {
                            formatter.format(
                                it
                            )
                        },
                        items = itemsMap[order.id].orEmpty().map { item ->
                            PurchaseOrderItemUi(
                                productId = item.productId,
                                productName = products[item.productId]?.name
                                    ?: "Producto #${item.productId}",
                                quantity = item.quantity,
                                suggestedUnitPriceInCents = products[item.productId]?.purchasePriceInCents
                            )
                        }
                    )
                }

                _state.update {
                    it.copy(
                        orders = uiOrders,
                        selectedOrder = uiOrders.firstOrNull()
                    )
                }
            }
        }
    }

    fun onEvent(event: PurchaseOrderEvent) {
        when (event) {

            is PurchaseOrderEvent.OnSearchChange -> {
                _state.update { it.copy(search = event.value) }
            }

            is PurchaseOrderEvent.OnStatusChange -> {
                _state.update { it.copy(selectedStatus = event.status) }
            }

            is PurchaseOrderEvent.OnSelectOrder -> {
                val order = _state.value.orders.firstOrNull { it.id == event.orderId }
                _state.update {
                    it.copy(
                        selectedOrder = order,
                        isDetailOpen = true
                    ) }
            }

            PurchaseOrderEvent.OnCloseDetail -> _state.update { it.copy(isDetailOpen = false) }

            PurchaseOrderEvent.OnMarkSent -> updateStatus(PurchaseOrderStatus.SENT)

            PurchaseOrderEvent.OnMarkReceived -> updateStatus(PurchaseOrderStatus.RECEIVED)

            PurchaseOrderEvent.OnCreateOrder -> {
                // navegación → se maneja desde el Route
            }

            else -> {}
        }
    }

    private fun updateStatus(status: PurchaseOrderStatus) {
        val order = domainOrders.firstOrNull { it.id == _state.value.selectedOrder?.id }
            ?: return

        val items = domainItems[order.id].orEmpty()

        viewModelScope.launch {
            val result = container.updatePurchaseOrderUseCase(
                purchaseOrder = order.copy(status = status),
                items = items
            )

            _state.update {
                it.copy(
                    feedback = if (result is ValidationResult.Success)
                        "Orden #${order.id} actualizada a ${status.name}"
                    else
                        "Error al actualizar"
                )
            }
        }
    }

    class PurchaseOrderViewModelFactory(
        private val container: AppContainer
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PurchaseOrderViewModel::class.java)) {
                return PurchaseOrderViewModel(container) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
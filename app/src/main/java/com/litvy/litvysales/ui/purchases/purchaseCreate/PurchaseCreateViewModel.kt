package com.litvy.litvysales.ui.purchases.purchaseCreate

import androidx.lifecycle.ViewModel
import com.litvy.litvysales.ui.util.model.PurchaseItemUi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PurchaseCreateViewModel : ViewModel() {

    private val _state = MutableStateFlow(PurchaseCreateState())
    val state: StateFlow<PurchaseCreateState> = _state

    fun onEvent(event: PurchaseCreateEvent) {

        when (event) {

            PurchaseCreateEvent.Cancel -> {
                // navegación futura
            }

            PurchaseCreateEvent.Confirm -> {
                confirmPurchase()
            }

            // abre dialog para agregar producto
            PurchaseCreateEvent.AddItem -> {

                _state.update {
                    it.copy(showAddProductDialog = true)
                }

            }

            // elimina producto de la lista
            is PurchaseCreateEvent.RemoveItem -> {

                _state.update { state ->

                    val newItems = state.items.filterNot {
                        it.uiId == event.itemId
                    }

                    val (subtotal, tax, total) = recalculateTotals(newItems)

                    state.copy(
                        items = newItems,
                        subtotal = subtotal,
                        tax = tax,
                        total = total
                    )
                }
            }

            is PurchaseCreateEvent.SelectProvider -> {

                _state.update {
                    it.copy(provider = event.provider)
                }

            }

            is PurchaseCreateEvent.SelectInvoiceType -> {

                _state.update {
                    it.copy(invoiceType = event.invoiceType)
                }

            }

            is PurchaseCreateEvent.SelectPaymentMethod -> {

                _state.update {
                    it.copy(paymentMethod = event.paymentMethod)
                }

            }


            else -> {}
        }

    }

    private fun confirmPurchase() {

        val state = _state.value

        // validaciones mínimas
        if (state.provider == null) return

        if (state.items.isEmpty()) return

        // acá luego irá la lógica de guardado
    }

    private fun recalculateTotals(items: List<PurchaseItemUi>): Triple<Long, Long, Long> {

        val subtotal = items.fold(0L) { acc, item ->
            acc + item.total
        }

        val tax = 0L

        val total = subtotal + tax

        return Triple(subtotal, tax, total)
    }

}
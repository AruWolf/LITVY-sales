package com.litvy.litvysales.ui.purchases.purchaseCreate.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.purchases.purchaseCreate.PurchaseCreateEvent
import com.litvy.litvysales.ui.purchases.purchaseCreate.PurchaseCreateState

@Composable
fun PurchaseOrderSection(
    state: PurchaseCreateState,
    onEvent: (PurchaseCreateEvent) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Orden de compra",
                style = MaterialTheme.typography.titleMedium
            )

            DropdownSelector(
                label = "Vincular orden",
                value = state.selectedPurchaseOrder?.let { "#${it.id} - ${it.providerName}" }.orEmpty(),
                options = state.purchaseOrders.map { "#${it.id} - ${it.providerName} (${it.status})" },
                error = null,
                onOptionSelected = { selected ->
                    val order = state.purchaseOrders.firstOrNull {
                        "#${it.id} - ${it.providerName} (${it.status})" == selected
                    }
                    onEvent(PurchaseCreateEvent.SelectPurchaseOrder(order))
                }
            )

            state.selectedPurchaseOrder?.let { order ->
                Text("Estado: ${order.status}")
                order.expectedDeliveryLabel?.let {
                    Text("Entrega estimada: $it")
                }
                Text("Items solicitados: ${order.items.size}")

                OutlinedButton(
                    onClick = { onEvent(PurchaseCreateEvent.SelectPurchaseOrder(null)) }
                ) {
                    Text("Desvincular orden")
                }
            }
        }
    }
}

@Composable
fun PurchaseOrderConflictDialog(
    onEvent: (PurchaseCreateEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onEvent(PurchaseCreateEvent.DismissPurchaseOrderConflict) },
        title = {
            Text("Cargar orden de compra")
        },
        text = {
            Text(
                "Ya hay productos cargados manualmente. Elegi si queres fusionar la orden con los items actuales o sobreescribir la carga existente."
            )
        },
        confirmButton = {
            Button(
                onClick = { onEvent(PurchaseCreateEvent.ApplyPurchaseOrderByMerge) }
            ) {
                Text("Fusionar")
            }
        },
        dismissButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onEvent(PurchaseCreateEvent.ApplyPurchaseOrderByOverwrite) }
                ) {
                    Text("Sobreescribir")
                }
                OutlinedButton(
                    onClick = { onEvent(PurchaseCreateEvent.DismissPurchaseOrderConflict) }
                ) {
                    Text("Cancelar")
                }
            }
        }
    )
}

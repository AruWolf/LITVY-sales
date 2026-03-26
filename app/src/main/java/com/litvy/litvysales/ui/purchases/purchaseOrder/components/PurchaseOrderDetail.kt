package com.litvy.litvysales.ui.purchases.purchaseOrder.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.purchases.purchaseOrder.PurchaseOrderEvent
import com.litvy.litvysales.ui.purchases.purchaseOrder.PurchaseOrderState
import com.litvy.litvysales.util.MoneyFormatter

@Composable
fun PurchaseOrderDetail(
    state: PurchaseOrderState,
    onEvent: (PurchaseOrderEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val order = state.selectedOrder

    Card(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Detalle de orden", style = MaterialTheme.typography.headlineSmall)

            if (order == null) {
                Text("Seleccioná una orden.")
                return@Column
            }

            Text("Proveedor: ${order.providerName}")
            Text("Estado: ${order.status}")

            order.expectedDeliveryLabel?.let {
                Text("Entrega estimada: $it")
            }

            Text("Productos", style = MaterialTheme.typography.titleMedium)

            order.items.forEach { item ->
                val price = item.suggestedUnitPriceInCents
                    ?.let { MoneyFormatter.formatFromCents(it) }
                    ?: "Sin precio"

                Text("${item.productName} - ${item.quantity} - $price")
            }

            state.feedback?.let {
                Text(it)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                if (order.status == "PENDING") {
                    Button(onClick = { onEvent(PurchaseOrderEvent.OnMarkSent) }) {
                        Text("Enviar")
                    }
                }

                if (order.status != "RECEIVED" && order.status != "CANCELLED") {
                    Button(onClick = { onEvent(PurchaseOrderEvent.OnMarkReceived) }) {
                        Text("Recibir")
                    }
                }
            }
        }
    }
}
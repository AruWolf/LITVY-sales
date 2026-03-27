package com.litvy.litvysales.ui.purchases.purchaseOrder.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.purchases.purchaseOrder.PurchaseOrderEvent
import com.litvy.litvysales.ui.purchases.purchaseOrder.PurchaseOrderState

@Composable
fun PurchaseOrderList(
    state: PurchaseOrderState,
    onEvent: (PurchaseOrderEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Órdenes de compra", style = MaterialTheme.typography.headlineSmall)

                Button(onClick = { onEvent(PurchaseOrderEvent.OnCreateOrder) }) {
                    Text("Crear")
                }
            }

            OutlinedTextField(
                value = state.search,
                onValueChange = { onEvent(PurchaseOrderEvent.OnSearchChange(it)) },
                label = { Text("Buscar orden o proveedor") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("ALL", "PENDING", "SENT", "RECEIVED", "CANCELLED")
                    .forEach { status ->
                        FilterChip(
                            selected = state.selectedStatus == status,
                            onClick = { onEvent(PurchaseOrderEvent.OnStatusChange(status)) },
                            label = { Text(if (status == "ALL") "Todas" else status) }
                        )
                    }
            }

            val filtered = state.orders.filter {
                (state.selectedStatus == "ALL" || it.status == state.selectedStatus) &&
                        (
                                state.search.isBlank() ||
                                        it.providerName.contains(state.search, true) ||
                                        it.id.toString().contains(state.search)
                                )
            }

            if (filtered.isEmpty()) {
                Text("No hay órdenes para los filtros actuales.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filtered, key = { it.id }) { order ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onEvent(PurchaseOrderEvent.OnSelectOrder(order.id))
                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text("#${order.id} - ${order.providerName}")
                                Text("Estado: ${order.status}")
                                order.expectedDeliveryLabel?.let {
                                    Text("Entrega: $it")
                                }
                                Text("Items: ${order.items.size}")
                            }
                        }
                    }
                }
            }
        }
    }
}
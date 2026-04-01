package com.litvy.litvysales.ui.purchases.purchaseCreate.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.util.model.PurchaseOrderUi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun PurchaseOrderDialog(
    orders: List<PurchaseOrderUi>,
    onSelect: (PurchaseOrderUi) -> Unit,
    onDismiss: () -> Unit
) {

    var search by remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val filteredOrders = orders.filter {
        search.isBlank() ||
                it.providerName.contains(search, ignoreCase = true) ||
                it.id.toString().contains(search)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth(
                    if (isLandscape) 0.9f else 0.95f
                )
                .heightIn(
                    min = 300.dp,
                    max = if (isLandscape) 600.dp else 500.dp
                )
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Seleccionar orden de compra",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(12.dp))

                if (isLandscape) {

                    Row(
                        modifier = Modifier.weight(1f)
                    ) {

                        Column(
                            modifier = Modifier
                                .weight(0.8f)
                                .padding(end = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = search,
                                onValueChange = { search = it },
                                label = { Text("Buscar") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        Card(
                            modifier = Modifier
                                .weight(1.4f)
                                .padding(4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            ) {
                                items(filteredOrders) { order ->
                                    OrderItemCard(order) {
                                        onSelect(order)
                                        onDismiss()
                                    }
                                }
                            }
                        }
                    }

                } else {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        OutlinedTextField(
                            value = search,
                            onValueChange = { search = it },
                            label = { Text("Buscar") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(Modifier.height(12.dp))

                        LazyColumn {
                            items(filteredOrders) { order ->
                                OrderItemCard(order) {
                                    onSelect(order)
                                    onDismiss()
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderItemCard(
    order: PurchaseOrderUi,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                "Orden #${order.id}",
                style = MaterialTheme.typography.titleSmall
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    "Proveedor: ${order.providerName}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    "Estado: ${order.status}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    "Items: ${order.items.size}",
                    style = MaterialTheme.typography.bodySmall
                )

                order.expectedDeliveryLabel?.let {
                    Text(
                        "Entrega: $it",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
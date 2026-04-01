package com.litvy.litvysales.ui.purchases.purchaseOrder.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.purchases.purchaseOrder.PurchaseOrderEvent
import com.litvy.litvysales.ui.purchases.purchaseOrder.PurchaseOrderState

@Composable
fun PurchaseOrderList(
    state: PurchaseOrderState,
    onEvent: (PurchaseOrderEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // COLUMNA IZQUIERDA (FILTROS)
                Card(
                    modifier = Modifier.weight(0.35f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(
                            "Filtros",
                            style = MaterialTheme.typography.titleSmall
                        )

                        OutlinedTextField(
                            value = state.search,
                            onValueChange = { onEvent(PurchaseOrderEvent.OnSearchChange(it)) },
                            label = { Text("Buscar") }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            maxItemsInEachRow = 2,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FlowRow(
                                maxItemsInEachRow = 2,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                listOf("PENDING", "SENT", "RECEIVED", "CANCELLED")
                                    .forEach { status ->

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.48f)
                                        ) {
                                            FilterChip(
                                                selected = state.selectedStatuses.contains(status),
                                                onClick = { onEvent(PurchaseOrderEvent.OnToggleStatus(status)) },
                                                label = {
                                                    Text(
                                                        status,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        textAlign = TextAlign.Center
                                                    )
                                                },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                            }
                        }
                    }
                }

                // COLUMNA DERECHA (LISTA ORDENES)
                Card(
                    modifier = Modifier.weight(0.65f)
                ) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(
                            "Órdenes",
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(Modifier.height(8.dp))

                        PurchaseOrderItems(
                            state = state,
                            onEvent = onEvent
                        )
                    }
                }
            }
        }

    // --------------------------------------VERTICAL-----------------------------------------------
    }else{
        Card(
            modifier = modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                OutlinedTextField(
                    value = state.search,
                    onValueChange = { onEvent(PurchaseOrderEvent.OnSearchChange(it)) },
                    label = { Text("Buscar orden o proveedor") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                FlowRow(
                    maxItemsInEachRow = 2,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FlowRow(
                        maxItemsInEachRow = 2,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("PENDING", "SENT", "RECEIVED", "CANCELLED")
                            .forEach { status ->

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.48f)
                                ) {
                                    FilterChip(
                                        selected = state.selectedStatuses.contains(status),
                                        onClick = { onEvent(PurchaseOrderEvent.OnToggleStatus(status)) },
                                        label = {
                                            Text(
                                                status,
                                                modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.Center
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                    }
                }

                val filtered = state.orders.filter {
                    (state.selectedStatuses.isEmpty() || it.status in state.selectedStatuses) &&
                            (
                                    state.search.isBlank() ||
                                            it.providerName.contains(state.search, true) ||
                                            it.id.toString().contains(state.search)
                                    )
                }

                if (filtered.isEmpty()) {
                    Text("No hay órdenes para los filtros actuales.")
                } else {
                    PurchaseOrderItems(
                        state = state,
                        onEvent = onEvent
                    )
                }
            }
        }
    }

}

@Composable
fun PurchaseOrderItems(
    state: PurchaseOrderState,
    onEvent: (PurchaseOrderEvent) -> Unit
) {
    val filtered = state.orders.filter {
        (state.selectedStatuses.isEmpty() || it.status in state.selectedStatuses) &&
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
                        },

                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline
                    )
                ) {
                    FlowRow(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("#${order.id}")
                        Text(order.providerName)
                        Text(order.status)

                        order.expectedDeliveryLabel?.let {
                            Text(it)
                        }

                        Text("Items: ${order.items.size}")
                    }
                }
            }
        }
    }
}
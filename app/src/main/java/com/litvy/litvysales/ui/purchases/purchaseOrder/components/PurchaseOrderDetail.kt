package com.litvy.litvysales.ui.purchases.purchaseOrder.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {

        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.9f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // IZQUIERDA
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        "Detalle de orden",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text("Proveedor: ${order?.providerName}")
                    Text("Estado: ${order?.status}")

                    order?.expectedDeliveryLabel?.let {
                        Text("Entrega estimada: $it")
                    }

                    Spacer(Modifier.height(8.dp))

                    if (order?.status != "RECEIVED" && order?.status != "CANCELLED") {

                        if (order?.status == "PENDING") {
                            Button(
                                onClick = { onEvent(PurchaseOrderEvent.OnMarkSent) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Enviar")
                            }
                        }

                        Button(
                            onClick = { /* TODO */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Registrar compra")
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { onEvent(PurchaseOrderEvent.OnCloseDetail) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar")
                    }
                }

                // DERECHA
                Card(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text("Productos", style = MaterialTheme.typography.titleMedium)

                        order?.items?.forEach {
                            val price = it.suggestedUnitPriceInCents
                                ?.let { MoneyFormatter.formatFromCents(it) }
                                ?: "Sin precio"

                            Text("${it.productName} - ${it.quantity} - $price")
                        }
                    }
                }
            }
        }
    }
else {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text("Detalle de orden", style = MaterialTheme.typography.headlineMedium)

                if (order == null) {
                    Text("Seleccioná una orden.")
                    return@Column
                }

                Text("Proveedor: ${order.providerName}", style = MaterialTheme.typography.bodyLarge)
                Text("Estado: ${order.status}", style = MaterialTheme.typography.bodyLarge)

                order.expectedDeliveryLabel?.let {
                    Text("Entrega estimada: $it")
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text("Productos", style = MaterialTheme.typography.titleMedium)

                        order.items.forEach { item ->
                            val price = item.suggestedUnitPriceInCents
                                ?.let { MoneyFormatter.formatFromCents(it) }
                                ?: "Sin precio"

                            Text("${item.productName} - ${item.quantity} - $price")
                        }
                    }
                }

                // PIE DE DIALOG CON BOTONES
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    if (order.status != "RECEIVED" && order.status != "CANCELLED") {

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            if (order.status == "PENDING") {
                                // BOTON DE ENVIAR
                                Button(
                                    onClick = { onEvent(PurchaseOrderEvent.OnMarkSent) },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Enviar")
                                }
                            }
                            // BOTON REGISTRAR COMPRA
                            Button(
                                onClick = { /* TODO */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Registrar compra")
                            }
                        }
                    }

                    // BOTÓN CERRAR
                    Button(
                        onClick = { onEvent(PurchaseOrderEvent.OnCloseDetail) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}
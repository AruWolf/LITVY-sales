package com.litvy.litvysales.ui.purchases.purchaseCreate

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import com.litvy.litvysales.ui.components.dialog.AddProductDialog
import com.litvy.litvysales.ui.purchases.purchaseCreate.components.*

@Composable
fun PurchaseCreateScreen(
    state: PurchaseCreateState,
    onEvent: (PurchaseCreateEvent) -> Unit,
    onBack: () -> Unit
) {
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        LandscapeContent(state, onEvent, onBack)
    } else {
        PortraitContent(state, onEvent, onBack)
    }

    if (state.showAddProductDialog) {
        AddProductDialog(
            state = state.addProductState,
            onEvent = { dialogEvent ->
                onEvent(PurchaseCreateEvent.AddProductDialog(dialogEvent))
            }
        )
    }

    if (state.showOrderConflictDialog) {
        PurchaseOrderConflictDialog(onEvent = onEvent)
    }

    if (state.showOrderDialog){
        PurchaseOrderDialog(
            orders = state.purchaseOrders
                .filter { it.status == "PENDING" || it.status == "SENT" },
            onSelect = {
                onEvent(PurchaseCreateEvent.SelectPurchaseOrder(it))
                onEvent(PurchaseCreateEvent.CloseOrderDialog)
            },
            onDismiss = {
                onEvent(PurchaseCreateEvent.CloseOrderDialog)
            }
        )
    }
}

@Composable
private fun PortraitContent(
    state: PurchaseCreateState,
    onEvent: (PurchaseCreateEvent) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Registrar compra",
                style = MaterialTheme.typography.headlineSmall
            )

            val isLinked = state.selectedPurchaseOrder != null

            Button(
                onClick = {
                    if (!isLinked) {
                        onEvent(PurchaseCreateEvent.OpenOrderDialog)
                    } else {
                        onEvent(PurchaseCreateEvent.SelectPurchaseOrder(null))
                    }
                },
                colors = if (isLinked) {
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                } else {
                    ButtonDefaults.buttonColors()
                }
            ) {
                Text(if (isLinked) "Desvincular" else "Vincular orden")
            }
        }

        state.selectedPurchaseOrder?.let { order ->
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Orden vinculada: #${order.id}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(8.dp))
        PurchaseHeader(state, onEvent)
        Spacer(Modifier.height(16.dp))
        Spacer(Modifier.height(16.dp))
        PurchaseItemsList(
            modifier = Modifier.weight(1f),
            items = state.items,
            itemsError = state.itemsError,
            onAddItem = { onEvent(PurchaseCreateEvent.OpenAddProductDialog) },
            onRemove = { onEvent(PurchaseCreateEvent.RemoveItem(it)) },
            onQuantityChange = { itemId, quantity ->
                onEvent(PurchaseCreateEvent.UpdateQuantity(itemId, quantity))
            },
            onUnitPriceChange = { itemId, price ->
                onEvent(PurchaseCreateEvent.UpdateUnitPrice(itemId, price))
            }
        )
        Spacer(Modifier.height(16.dp))
        PurchaseTotals(state)
        Spacer(Modifier.height(16.dp))
        PurchaseActions(canConfirm = state.canConfirm, onEvent = onEvent, onBack = onBack)
    }
}

@Composable
private fun LandscapeContent(
    state: PurchaseCreateState,
    onEvent: (PurchaseCreateEvent) -> Unit,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Registrar compra",
                    style = MaterialTheme.typography.headlineSmall
                )

                Button(
                    onClick = {
                        if (state.selectedPurchaseOrder == null) {
                            onEvent(PurchaseCreateEvent.OpenOrderDialog)
                        } else {
                            onEvent(PurchaseCreateEvent.SelectPurchaseOrder(null))
                        }
                    }
                ) {
                    Text(
                        if (state.selectedPurchaseOrder == null)
                            "Vincular orden"
                        else
                            "Desvincular"
                    )
                }
            }

            state.selectedPurchaseOrder?.let { order ->
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Orden vinculada: #${order.id}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            PurchaseHeader(state, onEvent)

            PurchaseTotals(state)
        }

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1.2f)
        ) {

            PurchaseItemsList(
                modifier = Modifier.weight(1f),
                items = state.items,
                itemsError = state.itemsError,
                onAddItem = { onEvent(PurchaseCreateEvent.OpenAddProductDialog) },
                onRemove = { onEvent(PurchaseCreateEvent.RemoveItem(it)) },
                onQuantityChange = { itemId, quantity ->
                    onEvent(PurchaseCreateEvent.UpdateQuantity(itemId, quantity))
                },
                onUnitPriceChange = { itemId, price ->
                    onEvent(PurchaseCreateEvent.UpdateUnitPrice(itemId, price))
                }
            )

            Spacer(Modifier.height(12.dp))

            PurchaseActions(
                canConfirm = state.canConfirm,
                onEvent = onEvent,
                onBack = onBack
            )
        }
    }
}
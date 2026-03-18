package com.litvy.litvysales.ui.purchases.purchaseCreate

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.components.dialog.AddProductDialog
import com.litvy.litvysales.ui.purchases.purchaseCreate.components.PurchaseActions
import com.litvy.litvysales.ui.purchases.purchaseCreate.components.PurchaseHeader
import com.litvy.litvysales.ui.purchases.purchaseCreate.components.PurchaseItemsList
import com.litvy.litvysales.ui.purchases.purchaseCreate.components.PurchaseOrderConflictDialog
import com.litvy.litvysales.ui.purchases.purchaseCreate.components.PurchaseOrderSection
import com.litvy.litvysales.ui.purchases.purchaseCreate.components.PurchaseTotals

@Composable
fun PurchaseCreateScreen(
    state: PurchaseCreateState,
    onEvent: (PurchaseCreateEvent) -> Unit
) {
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        LandscapeContent(state, onEvent)
    } else {
        PortraitContent(state, onEvent)
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
}

@Composable
private fun PortraitContent(
    state: PurchaseCreateState,
    onEvent: (PurchaseCreateEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PurchaseHeader(state, onEvent)
        Spacer(Modifier.height(16.dp))
        PurchaseOrderSection(state, onEvent)
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
        PurchaseActions(canConfirm = state.canConfirm, onEvent = onEvent)
    }
}

@Composable
private fun LandscapeContent(
    state: PurchaseCreateState,
    onEvent: (PurchaseCreateEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.95f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PurchaseHeader(state, onEvent)
            PurchaseOrderSection(state, onEvent)
            PurchaseTotals(state)
            PurchaseActions(canConfirm = state.canConfirm, onEvent = onEvent)
        }

        Spacer(Modifier.width(16.dp))

        PurchaseItemsList(
            modifier = Modifier.weight(1.2f),
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
    }
}

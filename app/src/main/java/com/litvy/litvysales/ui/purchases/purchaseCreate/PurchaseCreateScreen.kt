package com.litvy.litvysales.ui.purchases.purchaseCreate

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.components.dialog.AddProductDialog
import com.litvy.litvysales.ui.purchases.purchaseCreate.components.*

@Composable
fun PurchaseCreateScreen(
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

        PurchaseItemsList(
            items = state.items,
            onAddItem = { onEvent(PurchaseCreateEvent.AddItem) },
            onRemove = { itemId ->
                onEvent(PurchaseCreateEvent.RemoveItem(itemId))
            }
        )

        Spacer(Modifier.weight(1f))

        PurchaseTotals(state)

        Spacer(Modifier.height(16.dp))

        PurchaseActions(onEvent)
    }

    if (state.showAddProductDialog) {

        AddProductDialog(
            state = state.addProductState,
            onEvent = { /* manejar eventos */ }
        )

    }
}
package com.litvy.litvysales.ui.purchases.purchaseCreate.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.litvy.litvysales.ui.purchases.purchaseCreate.PurchaseCreateState

@Composable
fun PurchaseTotals(state: PurchaseCreateState) {

    Column {

        Text("Subtotal: ${state.subtotal}")
        Text("Impuestos: ${state.tax}")
        Text(
            "Total: ${state.total}",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
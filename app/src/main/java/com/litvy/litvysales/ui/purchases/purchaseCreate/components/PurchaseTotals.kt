package com.litvy.litvysales.ui.purchases.purchaseCreate.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.purchases.purchaseCreate.PurchaseCreateState
import com.litvy.litvysales.util.MoneyFormatter

@Composable
fun PurchaseTotals(state: PurchaseCreateState) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Resumen",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Subtotal: ${MoneyFormatter.formatFromCents(state.subtotalInCents)}")
            Text("Impuestos: ${MoneyFormatter.formatFromCents(state.taxInCents)}")
            Text(
                text = "Total: ${MoneyFormatter.formatFromCents(state.totalInCents)}",
                style = MaterialTheme.typography.titleLarge
            )

            state.feedbackMessage?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

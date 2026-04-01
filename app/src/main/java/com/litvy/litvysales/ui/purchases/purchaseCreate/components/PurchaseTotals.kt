package com.litvy.litvysales.ui.purchases.purchaseCreate.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.purchases.purchaseCreate.PurchaseCreateState
import com.litvy.litvysales.util.MoneyFormatter

@Composable
fun PurchaseTotals(state: PurchaseCreateState) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Resumen",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = if (expanded) "▲" else "▼",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "Total: ${MoneyFormatter.formatFromCents(state.totalInCents)}",
                style = MaterialTheme.typography.titleLarge
            )

            if (expanded) {
                Text("Subtotal: ${MoneyFormatter.formatFromCents(state.subtotalInCents)}")
                Text("Impuestos: ${MoneyFormatter.formatFromCents(state.taxInCents)}")
            }

            state.feedbackMessage?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

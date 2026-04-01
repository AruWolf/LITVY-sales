package com.litvy.litvysales.ui.purchases.purchaseCreate.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.purchases.purchaseCreate.PurchaseCreateEvent

@Composable
fun PurchaseActions(
    canConfirm: Boolean,
    onEvent: (PurchaseCreateEvent) -> Unit,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
    ) {
        OutlinedButton(
            onClick = {
                onEvent(PurchaseCreateEvent.Cancel)
                onBack()
            }
        ) {
            Text("Cancelar")
        }

        Button(
            enabled = canConfirm,
            onClick = { onEvent(PurchaseCreateEvent.Confirm) }
        ) {
            Text("Confirmar compra")
        }
    }
}

package com.litvy.litvysales.ui.purchases.purchaseCreate.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.litvy.litvysales.ui.purchases.purchaseCreate.PurchaseCreateEvent

@Composable
fun PurchaseActions(
    canConfirm: Boolean,
    onEvent: (PurchaseCreateEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            onClick = { onEvent(PurchaseCreateEvent.Cancel) }
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

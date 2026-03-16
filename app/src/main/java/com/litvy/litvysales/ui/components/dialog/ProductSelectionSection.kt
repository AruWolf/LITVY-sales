package com.litvy.litvysales.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProductSelectionSection(
    state: AddProductDialogState,
    onEvent: (AddProductDialogEvent) -> Unit
) {

    Column {

        Text(
            state.selectedProduct!!.name,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                value = state.quantity,
                onValueChange = {
                    onEvent(AddProductDialogEvent.QuantityChanged(it))
                },
                label = { Text("Cantidad") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = state.unitPrice,
                onValueChange = {
                    onEvent(AddProductDialogEvent.PriceChanged(it))
                },
                label = { Text("Precio") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
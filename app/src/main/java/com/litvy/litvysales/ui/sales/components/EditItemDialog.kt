package com.litvy.litvysales.ui.sales.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.sales.model.EditSaleItemState

@Composable
fun EditItemDialog(
    state: EditSaleItemState,
    onQuantityChange: (String) -> Unit,
    onUnitPriceChange: (String) -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar producto") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = state.name,
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = state.quantity,
                    onValueChange = onQuantityChange,
                    label = { Text("Cantidad") },
                    singleLine = true,
                    isError = state.quantityError != null,
                    modifier = Modifier.fillMaxWidth()
                )

                state.quantityError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = state.unitPrice,
                    onValueChange = onUnitPriceChange,
                    label = { Text("Precio") },
                    singleLine = true,
                    isError = state.unitPriceError != null,
                    modifier = Modifier.fillMaxWidth()
                )

                state.unitPriceError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onSave) {
                Text("Guardar cambios")
            }
        },
        dismissButton = {
            Column(
                modifier = Modifier.padding(end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = onDelete) {
                    Text("Eliminar item")
                }
                OutlinedButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
            }
        }
    )
}

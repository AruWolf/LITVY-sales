package com.litvy.litvysales.ui.components.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun AddProductDialog(
    state: AddProductDialogState,
    onEvent: (AddProductDialogEvent) -> Unit
) {

    AlertDialog(

        onDismissRequest = {
            onEvent(AddProductDialogEvent.Cancel)
        },

        confirmButton = {

            Button(
                onClick = {
                    onEvent(AddProductDialogEvent.Confirm)
                }
            ) {
                Text("Agregar")
            }
        },

        dismissButton = {

            OutlinedButton(
                onClick = {
                    onEvent(AddProductDialogEvent.Cancel)
                }
            ) {
                Text("Cancelar")
            }
        },

        text = {

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ProductSearchBar(state, onEvent)

                if (state.showResults) {
                    ProductSearchResults(state, onEvent)
                }

                state.selectedProduct?.let {

                    ProductSelectionSection(state, onEvent)

                }
            }
        }
    )
}
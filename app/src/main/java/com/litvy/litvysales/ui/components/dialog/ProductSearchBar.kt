package com.litvy.litvysales.ui.components.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProductSearchBar(
    state: AddProductDialogState,
    onEvent: (AddProductDialogEvent) -> Unit
) {

    OutlinedTextField(

        value = state.searchQuery,

        onValueChange = {
            onEvent(AddProductDialogEvent.SearchChanged(it))
        },

        label = { Text("Buscar producto") },

        trailingIcon = {

            IconButton(
                onClick = {
                    onEvent(AddProductDialogEvent.ScanBarcode)
                }
            ) {
                Icon(Icons.Default.CameraAlt, null)
            }
        },

        modifier = Modifier.fillMaxWidth()
    )
}
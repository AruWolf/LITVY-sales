package com.litvy.litvysales.ui.catalog.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import com.litvy.litvysales.ui.catalog.util.CatalogLevel
import com.litvy.litvysales.ui.catalog.util.getTitle

@Composable
fun CatalogCreateDialog(
    mode: CatalogFormMode,
    level: CatalogLevel,
    name: String,
    purchase: String,
    sale: String,
    hasExpiration: Boolean,
    isWeighable: Boolean,

    onNameChange: (String) -> Unit,
    onPurchaseChange: (String) -> Unit,
    onSaleChange: (String) -> Unit,
    onExpirationChange: (Boolean) -> Unit,
    onWeighableChange: (Boolean) -> Unit,

    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismiss,

        confirmButton = {
            Button(onClick = onConfirm) {
                Text(
                    if(mode == CatalogFormMode.CREATE)
                        "Crear"
                    else
                        "Guardar"
                )
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },

        title = {
            Text(
                if(mode == CatalogFormMode.CREATE)
                    "Crear ${getTitle(level)}"
                else
                    "Editar ${getTitle(level)}"
            )
        },

        text = {

            if(level == CatalogLevel.PRODUCTS){

                ProductForm(
                    name = name,
                    purchase = purchase,
                    sale = sale,
                    hasExpiration = hasExpiration,
                    isWeighable = isWeighable,

                    onNameChange = onNameChange,
                    onPurchaseChange = onPurchaseChange,
                    onSaleChange = onSaleChange,
                    onExpirationChange = onExpirationChange,
                    onWeighableChange = onWeighableChange
                )

            } else {

                TextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Nombre") }
                )

            }

        }

    )
}
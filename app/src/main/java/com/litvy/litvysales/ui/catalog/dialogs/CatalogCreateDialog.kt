package com.litvy.litvysales.ui.catalog.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.unit.dp
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
    errors: Map<String,String>,
    warnings: Map<String,String>,

    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Dialog(

        onDismissRequest = onDismiss,

        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )

    ) {

        Surface(

            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,

            modifier = Modifier
                .fillMaxWidth(
                    if (isLandscape) 0.75f else 0.92f
                )
                .widthIn(
                    max = if (isLandscape) 900.dp else 480.dp
                )

        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(

                    text =
                        if(mode == CatalogFormMode.CREATE)
                            "Crear ${getTitle(level)}"
                        else
                            "Editar ${getTitle(level)}",

                    style = MaterialTheme.typography.titleLarge

                )

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
                        onWeighableChange = onWeighableChange,

                        errors = errors,
                        warnings = warnings
                    )

                } else {

                    TextField(

                        value = name,
                        onValueChange = onNameChange,

                        label = { Text("Nombre") },

                        isError = errors["name"] != null,

                        supportingText = {
                            errors["name"]?.let {
                                Text(it)
                            }
                        },

                        modifier = Modifier.fillMaxWidth()

                    )

                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }

                    Spacer(Modifier.width(8.dp))

                    Button(onClick = onConfirm) {
                        Text(
                            if(mode == CatalogFormMode.CREATE)
                                "Crear"
                            else
                                "Guardar"
                        )
                    }

                }

            }

        }

    }

}
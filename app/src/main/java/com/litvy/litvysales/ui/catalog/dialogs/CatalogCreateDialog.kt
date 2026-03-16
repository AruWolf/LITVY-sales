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

// Metodo de creación de formulario para creación
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

    val configuration = LocalConfiguration.current // Valor para almacenar configuración del dispositivo
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE // Valor para definir orientación horizontal

    // Composable Dialog para emitir una ventana emergente,
    // utilizada para contener un formulario de creación/edición de elementos.
    Dialog(

        onDismissRequest = onDismiss, // Inicialización de variable local. Utilizada para activar el pedido de cierre del Compose dialog

        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )

    ) {
        // Compose Surface, utilizado para definir diseño del contenido del dialog
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

                // Titulo del dialog
                Text(

                    text =
                        if(mode == CatalogFormMode.CREATE) // Titulo para caso creación
                            "Crear ${getTitle(level)}"
                        else // Titulo para caso edición
                            "Editar ${getTitle(level)}",

                    style = MaterialTheme.typography.titleLarge

                )

                // Tipo de formulario para caso de productos
                if(level == CatalogLevel.PRODUCTS){

                    // Clase compose para generar el formulario para productos
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

                    // Tipo de formulario para casos categoria, subcategoria y marca
                } else {

                    // Al requerir la carga de una propiedad 'nombre' para cualquiera de los tres casos se utiliza un textfield
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

                // Contenedor de botones 'Cancelar' y 'Crear'/'Guardar'
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    // Botón cancelar
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }

                    Spacer(Modifier.width(8.dp))

                    // Botón crear/guardar
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
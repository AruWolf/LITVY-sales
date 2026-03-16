package com.litvy.litvysales.ui.catalog.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

// Metodo compose para formulario de creación/edición de productos
@Composable
fun ProductForm(

    // Propiedades requeridas para producto
    name: String,
    purchase: String,
    sale: String,
    hasExpiration: Boolean,
    isWeighable: Boolean,

    // Propiedades de control de errores y advertencias
    errors: Map<String, String>,
    warnings: Map<String, String>,

    // Propiedades para textFields
    onNameChange: (String) -> Unit,
    onPurchaseChange: (String) -> Unit,
    onSaleChange: (String) -> Unit,
    onExpirationChange: (Boolean) -> Unit,
    onWeighableChange: (Boolean) -> Unit

) {

    val configuration = LocalConfiguration.current // Configuración del dispositivo
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE // Valor de orientación horizontal

    // Contenedor compose de formulario para creación/edición de productos
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(
                // Extensión de ancho para orientación horizontal y para orientación vertical
                max = if (isLandscape) 700.dp else 420.dp
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Si el dispositivo se encuentra en orientación horizontal, se construye el formulario en base a la siguiente disposición.
        if (isLandscape) {

            // Contenedor de elementos del formulario, ordenados por fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                // Primera columna. Contiene los campos de 'nombre' y 'precios' de compra/venta.
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Campo de nombre
                    TextField(

                        value = name,
                        onValueChange = onNameChange,

                        label = { Text("Nombre del producto") },

                        modifier = Modifier.fillMaxWidth(),

                        singleLine = true,

                        isError = errors["name"] != null,

                        supportingText = {
                            errors["name"]?.let { Text(it) }
                        }

                    )

                    // Titulo para campos de precios
                    Text(
                        "Precios",
                        style = MaterialTheme.typography.titleSmall
                    )

                    // Compose para ordenar los campos de precio compra y venta en una misma fila.
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        // Campo para precio de compra
                        TextField(

                            value = formatPrice(purchase), // Formateo del precio

                            prefix = { Text("$") }, // Prefijo para el indicar cantidad monetaria

                            onValueChange = { input ->
                                val clean = input.filter { it.isDigit() }
                                onPurchaseChange(clean)
                            },

                            label = { Text("Compra") },

                            modifier = Modifier.weight(1f),

                            singleLine = true,

                            // Limitador de caracteres en el campo. Solo permite números
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),

                            isError = errors["purchase"] != null,

                            supportingText = {
                                errors["purchase"]?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                        )

                        // Campo para precio de venta.
                        TextField(

                            value = formatPrice(sale), // Formateo del precio

                            prefix = { Text("$") }, // Prefijo para indicar cantidad monetaria

                            onValueChange = { input ->
                                val clean = input.filter { it.isDigit() }
                                onSaleChange(clean)
                            },

                            label = { Text("Venta") },

                            modifier = Modifier.weight(1f),

                            singleLine = true,

                            // Limitador de caracteres. Permite solo cargar números
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),

                            isError = errors["sale"] != null,

                            // Texto utilizado para generar mensajes de error o advertencia
                            supportingText = {

                                when {
                                    errors["sale"] != null ->
                                        Text(
                                            text = errors["sale"]!!,
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                    warnings["sale"] != null ->
                                        Text(
                                            text = warnings["sale"]!!,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                }

                            }

                        )
                    }
                }

                // Segunda columna, utilizada para los campos booleanos, 'vencimiento' y 'pesable'
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Titulo de propiedades booleanas
                    Text(
                        "Opciones",
                        style = MaterialTheme.typography.titleSmall
                    )

                    // Botón switch para indicar si el producto posee vencimiento o no
                    SwitchOption(
                        text = "Tiene vencimiento",
                        checked = hasExpiration,
                        onChange = onExpirationChange
                    )

                    // Botón switch para indicar si el producto es pesable o no.
                    SwitchOption(
                        text = "Producto pesable",
                        checked = isWeighable,
                        onChange = onWeighableChange
                    )
                }
            }

            // Construcción del formulario en caso de utilizar orientación vertical
        } else {

            // Campo para nombre
            TextField(

                value = name,
                onValueChange = onNameChange,

                label = { Text("Nombre del producto") },

                modifier = Modifier.fillMaxWidth(),

                singleLine = true,

                isError = errors["name"] != null,

                supportingText = {
                    errors["name"]?.let { Text(it) }
                }

            )

            // Titulo para precios
            Text(
                "Precios",
                style = MaterialTheme.typography.titleSmall
            )

            // Compose para contener los campos de precios en una misma fila
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                // Campo de precio de compra
                TextField(

                    value = formatPrice(purchase), // Formateo de precio

                    prefix = { Text("$") }, // Prefijo para indicar valor monetario

                    onValueChange = { input ->
                        val clean = input.filter { it.isDigit() }
                        onPurchaseChange(clean)
                    },

                    label = { Text("Compra") },

                    modifier = Modifier.weight(1f),

                    singleLine = true,

                    // Limitador de caracteres. Permite solo caracteres númericos
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),

                    isError = errors["purchase"] != null,

                    supportingText = {
                        errors["purchase"]?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                )

                // Campo de precio de venta
                TextField(

                    value = formatPrice(sale), // Formateo de precio

                    prefix = { Text("$") }, // Prefijo para indicar valor monetario

                    onValueChange = { input ->
                        val clean = input.filter { it.isDigit() }
                        onSaleChange(clean)
                    },

                    label = { Text("Venta") },

                    modifier = Modifier.weight(1f),

                    singleLine = true,

                    // Limitador de caracteres. Permite solo caracteres numericos
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),

                    isError = errors["sale"] != null,

                    supportingText = {

                        when {
                            errors["sale"] != null ->
                                Text(
                                    text = errors["sale"]!!,
                                    style = MaterialTheme.typography.bodySmall
                                )

                            warnings["sale"] != null ->
                                Text(
                                    text = warnings["sale"]!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                        }

                    }

                )
            }

            // Titulo 'Opciones' para indicar sección de botones switch para propiedades booleanas
            Text(
                "Opciones",
                style = MaterialTheme.typography.titleSmall
            )

            // Compose para construir los switch en columnas
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                // Botón switch para indicar si el producto posee vencimiento o no.
                SwitchOption(
                    text = "Tiene vencimiento",
                    checked = hasExpiration,
                    onChange = onExpirationChange
                )

                // Botón switch para indicar si el producto es pesable o no.
                SwitchOption(
                    text = "Producto pesable",
                    checked = isWeighable,
                    onChange = onWeighableChange
                )

            }
        }
    }
}

// Metodo privado para generar botones switch para propiedades booleanas
@Composable
private fun SwitchOption(
    text: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Botón switch
        Switch(
            checked = checked,
            onCheckedChange = onChange
        )

        Spacer(Modifier.width(8.dp))

        // Texto para indicar propiedad manipulada por el switch
        Text(text)

    }
}

// Metodo privado para formatear el precio.
private fun formatPrice(value: String): String {

    if (value.isBlank()) return "" // Si no hay valor cargado, devolver ""

    val number = value.toLongOrNull() ?: return "" // Convertir valor a Long, en caso de que no se pueda, devolver ""

    val formatter = NumberFormat.getNumberInstance(Locale("es", "AR")) // Numero formato Español Argentina

    return formatter.format(number) // Devuelve el numero formateado.

}
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

@Composable
fun ProductForm(

    name: String,
    purchase: String,
    sale: String,
    hasExpiration: Boolean,
    isWeighable: Boolean,

    errors: Map<String, String>,
    warnings: Map<String, String>,

    onNameChange: (String) -> Unit,
    onPurchaseChange: (String) -> Unit,
    onSaleChange: (String) -> Unit,
    onExpirationChange: (Boolean) -> Unit,
    onWeighableChange: (Boolean) -> Unit

) {

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(
                max = if (isLandscape) 700.dp else 420.dp
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        if (isLandscape) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

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

                    Text(
                        "Precios",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        TextField(

                            value = formatPrice(purchase),

                            prefix = { Text("$") },

                            onValueChange = { input ->
                                val clean = input.filter { it.isDigit() }
                                onPurchaseChange(clean)
                            },

                            label = { Text("Compra") },

                            modifier = Modifier.weight(1f),

                            singleLine = true,

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

                        TextField(

                            value = formatPrice(sale),

                            prefix = { Text("$") },

                            onValueChange = { input ->
                                val clean = input.filter { it.isDigit() }
                                onSaleChange(clean)
                            },

                            label = { Text("Venta") },

                            modifier = Modifier.weight(1f),

                            singleLine = true,

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
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        "Opciones",
                        style = MaterialTheme.typography.titleSmall
                    )

                    SwitchOption(
                        text = "Tiene vencimiento",
                        checked = hasExpiration,
                        onChange = onExpirationChange
                    )

                    SwitchOption(
                        text = "Producto pesable",
                        checked = isWeighable,
                        onChange = onWeighableChange
                    )
                }
            }

        } else {

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

            Text(
                "Precios",
                style = MaterialTheme.typography.titleSmall
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                TextField(

                    value = formatPrice(purchase),

                    prefix = { Text("$") },

                    onValueChange = { input ->
                        val clean = input.filter { it.isDigit() }
                        onPurchaseChange(clean)
                    },

                    label = { Text("Compra") },

                    modifier = Modifier.weight(1f),

                    singleLine = true,

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

                TextField(

                    value = formatPrice(sale),

                    prefix = { Text("$") },

                    onValueChange = { input ->
                        val clean = input.filter { it.isDigit() }
                        onSaleChange(clean)
                    },

                    label = { Text("Venta") },

                    modifier = Modifier.weight(1f),

                    singleLine = true,

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

            Text(
                "Opciones",
                style = MaterialTheme.typography.titleSmall
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                SwitchOption(
                    text = "Tiene vencimiento",
                    checked = hasExpiration,
                    onChange = onExpirationChange
                )

                SwitchOption(
                    text = "Producto pesable",
                    checked = isWeighable,
                    onChange = onWeighableChange
                )

            }
        }
    }
}

@Composable
private fun SwitchOption(
    text: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Switch(
            checked = checked,
            onCheckedChange = onChange
        )

        Spacer(Modifier.width(8.dp))

        Text(text)

    }
}

private fun formatPrice(value: String): String {

    if (value.isBlank()) return ""

    val number = value.toLongOrNull() ?: return ""

    val formatter = NumberFormat.getNumberInstance(Locale("es", "AR"))

    return formatter.format(number)

}
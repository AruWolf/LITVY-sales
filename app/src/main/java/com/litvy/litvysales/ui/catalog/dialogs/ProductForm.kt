package com.litvy.litvysales.ui.catalog.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import com.litvy.litvysales.util.formatPrice

@Composable
fun ProductForm(

    name: String,
    purchase: String,
    sale: String,
    hasExpiration: Boolean,
    isWeighable: Boolean,

    onNameChange: (String) -> Unit,
    onPurchaseChange: (String) -> Unit,
    onSaleChange: (String) -> Unit,
    onExpirationChange: (Boolean) -> Unit,
    onWeighableChange: (Boolean) -> Unit

) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Nombre producto
        TextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Precios
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

                label = { Text("Precio de compra") },

                modifier = Modifier.weight(1f),

                singleLine = true,

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )

            )

            TextField(
                value = formatPrice(sale),
                prefix = { Text("$") },

                onValueChange = { input ->

                    val clean = input.filter { it.isDigit() }

                    onSaleChange(clean)

                },

                label = { Text("Precio de venta") },

                modifier = Modifier.weight(1f),

                singleLine = true,

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )

            )

        }

        // Switches
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Switch(
                    checked = hasExpiration,
                    onCheckedChange = onExpirationChange
                )

                Spacer(Modifier.width(8.dp))

                Text("Controla vencimiento")

            }

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Switch(
                    checked = isWeighable,
                    onCheckedChange = onWeighableChange
                )

                Spacer(Modifier.width(8.dp))

                Text("Producto pesable")

            }

        }

    }

}
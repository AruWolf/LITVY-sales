package com.litvy.litvysales.ui.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProductListScreen(
    state: CatalogState,
    onBack: () -> Unit,
    onCreate: (String, Long, Long, Boolean, Boolean) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(onClick = onBack) {
            Text("← Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showDialog = true }) {
            Text("Crear producto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProductTable(state = state)

    }

    if (showDialog) {

        var name by remember { mutableStateOf("") }
        var purchase by remember { mutableStateOf("") }
        var sale by remember { mutableStateOf("") }

        var hasExpiration by remember { mutableStateOf(false) }
        var isWeighable by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showDialog = false },

            confirmButton = {

                Button(
                    onClick = {

                        onCreate(
                            name,
                            (purchase.toDouble() * 100).toLong(),
                            (sale.toDouble() * 100).toLong(),
                            hasExpiration,
                            isWeighable
                        )

                        showDialog = false
                    }
                ) {
                    Text("Crear")
                }

            },

            text = {

                Column {

                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre del producto") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = purchase,
                        onValueChange = { purchase = it },
                        label = { Text("Precio de compra") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = sale,
                        onValueChange = { sale = it },
                        label = { Text("Precio de venta") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text("Controla vencimiento")

                        Switch(
                            checked = hasExpiration,
                            onCheckedChange = { hasExpiration = it }
                        )

                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text("Producto pesable")

                        Switch(
                            checked = isWeighable,
                            onCheckedChange = { isWeighable = it }
                        )

                    }

                }

            }
        )
    }
}
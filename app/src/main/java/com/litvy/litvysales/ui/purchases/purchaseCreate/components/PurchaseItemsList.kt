package com.litvy.litvysales.ui.purchases.purchaseCreate.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.util.model.PurchaseItemUi
import com.litvy.litvysales.util.MoneyFormatter

@Composable
fun PurchaseItemsList(
    modifier: Modifier = Modifier,
    items: List<PurchaseItemUi>,
    itemsError: String?,
    onAddItem: () -> Unit,
    onRemove: (String) -> Unit,
    onQuantityChange: (String, String) -> Unit,
    onUnitPriceChange: (String, String) -> Unit
) {
    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Productos de la compra",
                style = MaterialTheme.typography.titleMedium
            )

            Button(onClick = onAddItem) {
                Text("+ Agregar")
            }
        }

        Spacer(Modifier.height(8.dp))

        if (items.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "Todavia no cargaste productos en la compra.",
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(items, key = { it.uiId }) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = item.productName,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                IconButton(
                                    onClick = { onRemove(item.uiId) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar producto"
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = item.quantity.toString(),
                                    onValueChange = { onQuantityChange(item.uiId, it) },
                                    label = { Text("Cantidad") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = item.unitPrice.toString(),
                                    onValueChange = { onUnitPriceChange(item.uiId, it) },
                                    label = { Text("Precio c/u") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }

                            Text(
                                text = "Importe: ${MoneyFormatter.formatFromCents(item.total)}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        itemsError?.let {
            Spacer(Modifier.height(8.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

package com.litvy.litvysales.ui.purchases.purchaseCreate.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.util.model.PurchaseItemUi

@Composable
fun PurchaseItemsList(
    items: List<PurchaseItemUi>,
    onAddItem: () -> Unit,
    onRemove: (String) -> Unit
) {

    Column {

        Text(
            "Productos",
            style = MaterialTheme.typography.titleMedium
        )

        LazyColumn {

            items(items) { item ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {

                    Text(
                        item.productName,
                        modifier = Modifier.weight(2f)
                    )

                    Text(
                        item.quantity.toString(),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        item.unitPrice.toString(),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        item.total.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
                IconButton(
                    onClick = { onRemove(item.uiId) }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                }
            }

        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = onAddItem) {
            Text("Agregar producto")
        }
    }
}
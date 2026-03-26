package com.litvy.litvysales.ui.purchases.purchaseOrder.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.util.model.PurchaseOrderItemUi
import com.litvy.litvysales.util.MoneyFormatter

@Composable
fun PurchaseOrderItemCard(
    item: PurchaseOrderItemUi,
    onSelectProduct: () -> Unit,
    onQuantityChange: (String) -> Unit,
    onRemove: () -> Unit
) {

    val price = item.suggestedUnitPriceInCents ?: 0
    val subtotal = (price * item.quantity).toLong()

    Card {
        Column(Modifier.padding(12.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                // Producto
                Box(modifier = Modifier.weight(1f)) {

                    OutlinedTextField(
                        value = item.productName,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Producto") }
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                onSelectProduct()
                            }
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Cantidad
                OutlinedTextField(
                    value = item.quantity.toString(),
                    onValueChange = onQuantityChange,
                    modifier = Modifier.width(80.dp),
                    label = { Text("Cant.") },
                    singleLine = true
                )

                Spacer(Modifier.width(8.dp))

                // Eliminar
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }

            Spacer(Modifier.height(8.dp))

            Text("Precio: ${MoneyFormatter.formatFromCents(price)}")
            Text("Subtotal: ${MoneyFormatter.formatFromCents(subtotal)}")
        }
    }
}
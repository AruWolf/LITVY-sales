package com.litvy.litvysales.ui.purchases.shoppingList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.purchases.shoppingList.ShoppingListEvent
import com.litvy.litvysales.ui.purchases.shoppingList.ShoppingListUiState

@Composable
fun ShoppingListTable(
    state: ShoppingListUiState,
    onEvent: (ShoppingListEvent) -> Unit
) {
    LazyColumn {

        state.groupedByBrand.forEach { (brand, items) ->

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = brand,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(8.dp)
                    )

                    Button(
                        onClick = {
                            onEvent(
                                ShoppingListEvent.OnCreateOrderFromBrand(brand)
                            )
                        }
                    ) {
                        Text("Generar orden")
                    }
                }
            }


            // ENCABEZADO COLUMNAS
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    TableCell("Nombre", 2f, true)
                    TableCell("Stock", 1f, true)
                    TableCell("Sugerido", 1f, true)
                    TableCell("Proveedor", 1f, true)
                }
            }

            // FILAS DE TABLA
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    TableCell(item.name, 2f)
                    TableCell(item.currentStock.toString(), 1f)
                    TableCell(item.suggestedQuantity.toString(), 1f)
                    TableCell(item.provider ?: "-", 1f)
                }
            }

            item {
                HorizontalDivider(modifier = Modifier
                    .height(2.dp)
                    .background(Color.Black))
            }
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    isHeader: Boolean = false
) {
    Text(
        text = text,
        modifier = Modifier
            .weight(weight)
            .padding(6.dp),
        style = if (isHeader) {
            MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            )
        } else {
            MaterialTheme.typography.bodyMedium
        }
    )
}
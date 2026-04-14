package com.litvy.litvysales.ui.catalog.body

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.catalog.util.CatalogState

@Composable
fun ProductTable(
    state: CatalogState,
    modifier: Modifier = Modifier,
    onEdit: (Int) -> Unit
) {

    // Tabla
    Column(modifier = modifier.fillMaxHeight()) {

        // Header fijo de la tabla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
        ) {

            Text("Producto", modifier = Modifier.weight(2f))
            Text("Compra", modifier = Modifier.weight(1f))
            Text("Venta", modifier = Modifier.weight(1f))

        }

        Divider()

        // Lista scrolleable
        LazyColumn {

            items(state.products) { product ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(0.1f),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { onEdit(product.id!!) },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar"
                            )
                        }
                    }

                    //Columna de nombre
                    Text(product.name, modifier = Modifier.weight(2f))

                    //Columna de precio de compra
                    Text(
                        String.format("%.2f", product.purchasePriceInCents / 100.0),
                        modifier = Modifier.weight(1f)
                    )

                    //Columna de precio de venta
                    Text(
                        String.format("%.2f", product.salePriceInCents / 100.0),
                        modifier = Modifier.weight(1f)
                    )


                }

            }

        }

    }

}
package com.litvy.litvysales.ui.catalog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProductTable(
    state: CatalogState,
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        modifier = modifier
            .fillMaxHeight()
    ) {

        item {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Text("Producto", modifier = Modifier.weight(2f))
                Text("Compra", modifier = Modifier.weight(1f))
                Text("Venta", modifier = Modifier.weight(1f))

            }

        }

        items(state.products) { product ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                Text(product.name, modifier = Modifier.weight(2f))

                Text(
                    (product.purchasePriceInCents / 100.0).toString(),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    (product.salePriceInCents / 100.0).toString(),
                    modifier = Modifier.weight(1f)
                )

            }

        }

    }

}
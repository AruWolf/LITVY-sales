package com.litvy.litvysales.ui.catalog.body

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.catalog.util.CatalogState

@Composable
fun ProductTable(
    state: CatalogState,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier.fillMaxHeight()) {

        // HEADER FIJO
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

        // LISTA SCROLLEABLE
        LazyColumn {

            items(state.products) { product ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    Text(product.name, modifier = Modifier.weight(2f))

                    Text(
                        String.format("%.2f", product.purchasePriceInCents / 100.0),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        String.format("%.2f", product.salePriceInCents / 100.0),
                        modifier = Modifier.weight(1f)
                    )

                }

            }

        }

    }

}
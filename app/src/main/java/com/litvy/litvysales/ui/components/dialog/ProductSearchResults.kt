package com.litvy.litvysales.ui.components.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.util.MoneyFormatter

@Composable
fun ProductSearchResults(
    state: AddProductDialogState,
    onEvent: (AddProductDialogEvent) -> Unit
) {

    LazyColumn(
        modifier = Modifier.height(200.dp)
    ) {

        items(state.products) { product ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onEvent(
                            AddProductDialogEvent.SelectProduct(product)
                        )
                    }
                    .padding(8.dp)
            ) {

                Text(
                    product.name,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    MoneyFormatter.formatFromCents(product.purchasePrice)
                )
            }
        }
    }
}
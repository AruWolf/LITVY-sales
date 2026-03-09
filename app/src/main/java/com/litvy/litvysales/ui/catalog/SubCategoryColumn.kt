package com.litvy.litvysales.ui.catalog

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubCategoryColumn(
    state: CatalogState,
    onClick: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = onBack
    ) {
        Text("← Volver")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
    ) {

        items(state.subCategories) { sub ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = { onClick(sub.id!!) }
            ) {

                Text(
                    sub.name,
                    modifier = Modifier.padding(16.dp)
                )

            }

        }

    }

}
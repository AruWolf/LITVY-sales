package com.litvy.litvysales.ui.catalog.body

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.catalog.util.CatalogState

@Composable
fun ProductListScreen(
    state: CatalogState,
    onCreate: (String, Long, Long, Boolean, Boolean) -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        ProductTable(state = state)

    }

}
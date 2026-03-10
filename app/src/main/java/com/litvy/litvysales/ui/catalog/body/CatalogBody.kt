package com.litvy.litvysales.ui.catalog.body

import androidx.compose.runtime.Composable
import com.litvy.litvysales.ui.catalog.util.CatalogLevel
import com.litvy.litvysales.ui.catalog.util.CatalogState
import com.litvy.litvysales.ui.catalog.util.getItems

@Composable
fun CatalogBody(
    state: CatalogState,
    onItemClick: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onInspect: (Int) -> Unit
) {

    if(state.level == CatalogLevel.PRODUCTS){

        ProductListScreen(
            state = state,
            onCreate = { name, purchase, sale, hasExpiration, isWeighable ->
                // delega a quien llame
            }
        )

    } else {

        CatalogGrid(

            items = getItems(state),

            onItemClick = onItemClick,

            onEdit = onEdit,

            onInspect = onInspect

        )

    }

}
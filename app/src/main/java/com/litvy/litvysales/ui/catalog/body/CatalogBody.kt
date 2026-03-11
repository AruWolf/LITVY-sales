package com.litvy.litvysales.ui.catalog.body

import androidx.compose.runtime.Composable
import com.litvy.litvysales.ui.catalog.util.CatalogLevel
import com.litvy.litvysales.ui.catalog.util.CatalogState
import com.litvy.litvysales.ui.catalog.util.getItems

// Cuerpo/Contenido del catalogo
@Composable
fun CatalogBody(
    state: CatalogState,
    onItemClick: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onInspect: (Int) -> Unit
) {

    // Construye la pantalla para los items de producto si nos encontramos en dicho nivel
    if(state.level == CatalogLevel.PRODUCTS){

        ProductListScreen(
            state = state
        )

    } else { // De lo contrario, se construyen los items mediante el diseño de grilla

        CatalogGrid(

            items = getItems(state), // Items que se obtienen del estado, que consulta los objetos creados en base de datos

            onItemClick = onItemClick, // Evento de click sobre el item

            onEdit = onEdit, // Evento de edición de item

            onInspect = onInspect // Evento de inspección del item

        )

    }

}
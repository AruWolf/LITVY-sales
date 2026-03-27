package com.litvy.litvysales.ui.purchases.purchaseOrder.create

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.domain.model.catalog.ProductWithBrand
import com.litvy.litvysales.util.MoneyFormatter

// Elemento compose para generar lista desplegable de seleccion de elementos
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: @Composable () -> Unit,
    items: List<String>,
    selectedValue: String,
    onSelect: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        // Campo para ingresar el elemento seleccionado
        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = label,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            }
        )

        // Lista desplegable de elementos disponibles para selección
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // Iterador para generar un item de lista por cada elemento disponible encontrado
            items.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = { Text(text) },
                    onClick = {
                        selected = text
                        expanded = false
                        onSelect(index)
                    }
                )
            }
        }
    }
}

// Generador de campos de busqueda y filtros
@Composable
fun SearchAndFilters(
    state: PurchaseOrderCreateState,
    onEvent: (PurchaseOrderCreateEvent) -> Unit
) {

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        // Buscador
        OutlinedTextField(
            value = state.productSearch,
            onValueChange = {
                onEvent(PurchaseOrderCreateEvent.OnProductSearchChange(it))
            },
            label = { Text("Buscar producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            Box(Modifier.weight(1f)) {
                DropdownSelector(
                    label = {Text("Categoría")},
                    items = state.categories.map { it.name },
                    selectedValue = state.categories
                        .firstOrNull { it.id == state.selectedCategoryId }
                        ?.name ?: "",
                    onSelect = { index ->
                        state.categories[index].id?.let {
                            onEvent(PurchaseOrderCreateEvent.OnCategorySelected(it))
                        }
                    }
                )
            }

            Box(Modifier.weight(1f)) {
                DropdownSelector(
                    label = {
                        Text(
                            text = "Subcategoría",
                            maxLines = 2,
                            style = MaterialTheme.typography.bodySmall
                        )
                    },
                    items = state.subCategories.map { it.name },
                    selectedValue = state.subCategories
                        .firstOrNull { it.id == state.selectedSubCategoryId }
                        ?.name ?: "",
                    onSelect = { index ->
                        state.subCategories[index].id?.let {
                            onEvent(PurchaseOrderCreateEvent.OnSubCategorySelected(it))
                        }
                    }
                )
            }
        }

        DropdownSelector(
            label = {Text("Marca (Opcional)")},
            items = listOf("Todas") + state.brands.map { it.name },
            selectedValue = state.brands
                .firstOrNull { it.id == state.selectedBrandId }
                ?.name ?: "",
            onSelect = { index ->
                if (index == 0) {
                    onEvent(PurchaseOrderCreateEvent.OnBrandSelected(null))
                } else {
                    state.brands[index - 1].id?.let {
                        onEvent(PurchaseOrderCreateEvent.OnBrandSelected(it))
                    }
                }
            }
        )
    }
}

// Generador de Lista de productos
@Composable
fun ProductList(
    products: List<ProductWithBrand>,
    state: PurchaseOrderCreateState,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    // Mensajes para el caso de lista vacia/sin resultados
    if (products.isEmpty()) {

        val message = when {
            state.selectedCategoryId == null && state.productSearch.isBlank() ->
                "Seleccioná una categoría o utilizá el buscador"

            state.selectedSubCategoryId == null && state.productSearch.isBlank() ->
                "Seleccioná una subcategoría o utilizá el buscador"

            else ->
                "Sin productos"
        }

        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text(message)
        }

        return
    }

    // Lista scrolleable de productos
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        // Iteración para la generación de cada producto encontrado
        items(products) { product ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        product.id?.let(onSelect)
                    }
                    .padding(12.dp)
            ) {

                // Nombre del producto
                Text(product.name)

                // Precios de compra y venta
                val purchase = product.purchasePriceInCents ?: 0
                val sale = product.salePriceInCents ?: 0

                Text(
                    "Compra: ${MoneyFormatter.formatFromCents(purchase)}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    "Venta: ${MoneyFormatter.formatFromCents(sale)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
    }
}

package com.litvy.litvysales.ui.purchases.purchaseOrder.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.domain.model.catalog.ProductWithBrand
import com.litvy.litvysales.util.MoneyFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
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

        OutlinedTextField(
            value = selectedValue,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
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
                    label = "Categoría",
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
                    label = "Subcategoría",
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
            label = "Marca (opcional)",
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

@Composable
fun ProductList(
    products: List<ProductWithBrand>,
    state: PurchaseOrderCreateState,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

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

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        items(products) { product ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        product.id?.let(onSelect)
                    }
                    .padding(12.dp)
            ) {

                Text(product.name)

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

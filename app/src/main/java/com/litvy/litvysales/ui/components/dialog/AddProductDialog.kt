package com.litvy.litvysales.ui.components.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.util.model.CatalogOptionUi
import com.litvy.litvysales.util.MoneyFormatter

@Composable
fun AddProductDialog(
    state: AddProductDialogState,
    onEvent: (AddProductDialogEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onEvent(AddProductDialogEvent.Cancel) },
        title = { Text("Agregar producto") },
        confirmButton = {
            Button(onClick = { onEvent(AddProductDialogEvent.Confirm) }) {
                Text("Agregar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onEvent(AddProductDialogEvent.Cancel) }) {
                Text("Cancelar")
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProductSearchBar(state, onEvent)
                ProductCatalogFilters(state, onEvent)

                state.searchError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (state.showResults) {
                    ProductSearchResults(state, onEvent)
                }

                state.selectedProduct?.let {
                    ProductSelectionSection(state, onEvent)
                }
            }
        }
    )
}

@Composable
private fun ProductSearchBar(
    state: AddProductDialogState,
    onEvent: (AddProductDialogEvent) -> Unit
) {
    OutlinedTextField(
        value = state.searchQuery,
        onValueChange = { onEvent(AddProductDialogEvent.SearchChanged(it)) },
        label = { Text("Buscar producto") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Buscar")
        },
        trailingIcon = {
            IconButton(onClick = { onEvent(AddProductDialogEvent.ScanBarcode) }) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Escanear")
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ProductCatalogFilters(
    state: AddProductDialogState,
    onEvent: (AddProductDialogEvent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterDropdown(
            label = "Categoria",
            value = state.categories.firstOrNull { it.id == state.selectedCategoryId }?.name.orEmpty(),
            options = state.categories,
            modifier = Modifier.weight(1f),
            onSelected = { option ->
                onEvent(AddProductDialogEvent.SelectCategory(option?.id))
            }
        )

        FilterDropdown(
            label = "Subcategoria",
            value = state.subCategories.firstOrNull { it.id == state.selectedSubCategoryId }?.name.orEmpty(),
            options = state.subCategories,
            modifier = Modifier.weight(1f),
            onSelected = { option ->
                onEvent(AddProductDialogEvent.SelectSubCategory(option?.id))
            }
        )
    }

    FilterDropdown(
        label = "Marca",
        value = state.brands.firstOrNull { it.id == state.selectedBrandId }?.name.orEmpty(),
        options = state.brands,
        modifier = Modifier.fillMaxWidth(),
        onSelected = { option ->
            onEvent(AddProductDialogEvent.SelectBrand(option?.id))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterDropdown(
    label: String,
    value: String,
    options: List<CatalogOptionUi>,
    modifier: Modifier = Modifier,
    onSelected: (CatalogOptionUi?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Todos") },
                onClick = {
                    expanded = false
                    onSelected(null)
                }
            )

            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        expanded = false
                        onSelected(option)
                    }
                )
            }
        }
    }
}

@Composable
private fun ProductSearchResults(
    state: AddProductDialogState,
    onEvent: (AddProductDialogEvent) -> Unit
) {
    if (state.products.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "No hay productos para los filtros elegidos.",
                modifier = Modifier.padding(16.dp)
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.heightIn(max = 220.dp)
    ) {
        items(state.products, key = { it.id }) { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "${product.categoryName} / ${product.subCategoryName} / ${product.brandName}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Compra sugerida: ${MoneyFormatter.formatFromCents(product.purchasePrice)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Button(
                        onClick = { onEvent(AddProductDialogEvent.SelectProduct(product)) }
                    ) {
                        Text("Seleccionar")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductSelectionSection(
    state: AddProductDialogState,
    onEvent: (AddProductDialogEvent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = state.selectedProduct!!.name,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "${state.selectedProduct.categoryName} / ${state.selectedProduct.subCategoryName} / ${state.selectedProduct.brandName}",
            style = MaterialTheme.typography.bodySmall
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = state.quantity,
                onValueChange = { onEvent(AddProductDialogEvent.QuantityChanged(it)) },
                label = { Text("Cantidad") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = state.quantityError != null
            )

            OutlinedTextField(
                value = state.unitPrice,
                onValueChange = { onEvent(AddProductDialogEvent.PriceChanged(it)) },
                label = { Text("Precio") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = state.unitPriceError != null
            )
        }

        state.quantityError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        state.unitPriceError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

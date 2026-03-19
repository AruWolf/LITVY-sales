package com.litvy.litvysales.ui.purchases.shoppingList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.LitvySalesApplication
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

@Composable
fun ShoppingListScreen() {
    val application = LocalContext.current.applicationContext as LitvySalesApplication
    val container = application.container

    var items by remember { mutableStateOf<List<ShoppingListItem>>(emptyList()) }
    var search by remember { mutableStateOf("") }

    LaunchedEffect(container) {
        combine(
            container.getActiveProductsUseCase(),
            container.database.inventoryDao().getAllProductsInventory()
        ) { products, inventoryList ->
            val inventoryByProduct = inventoryList.associateBy { it.productId }
            products.mapNotNull { product ->
                val productId = product.id ?: return@mapNotNull null
                val inventory = inventoryByProduct[productId]
                ShoppingListItem(
                    id = productId,
                    name = product.name,
                    currentStock = inventory?.stock ?: 0.0,
                    suggestedQuantity = 0.0,
                    provider = null
                )
            }
        }.collect { loadedItems ->
            items = loadedItems
        }
    }

    val filtered = items.filter {
        search.isBlank() || it.name.contains(search, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Lista de compras", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Buscar producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (filtered.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (items.isEmpty()) {
                        "No hay productos activos para mostrar."
                    } else {
                        "No hay productos que coincidan con la busqueda."
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtered, key = { it.id }) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(item.name, style = MaterialTheme.typography.titleMedium)
                            Text("Stock actual: ${item.currentStock}")
                            Text("Cantidad sugerida: ${item.suggestedQuantity}")
                            Text("Proveedor asociado: ${item.provider ?: "null"}")
                        }
                    }
                }
            }
        }
    }
}

private data class ShoppingListItem(
    val id: Int,
    val name: String,
    val currentStock: Double,
    val suggestedQuantity: Double,
    val provider: String?
)

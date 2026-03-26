package com.litvy.litvysales.ui.purchases.shoppingList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.litvy.litvysales.ui.purchases.shoppingList.components.TableCell
import androidx.navigation.NavHostController
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.ui.purchases.shoppingList.components.ShoppingListTable

@Composable
fun ShoppingListScreenRoute(
    navController: NavHostController
) {
    val context = LocalContext.current.applicationContext as LitvySalesApplication
    val container = context.container

    val viewModel: ShoppingListViewModel = viewModel(
        factory = ShoppingListViewModel.ShoppingListViewModelFactory(container)
    )

    val state by viewModel.state.collectAsState()

    ShoppingListScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = {
            navController.popBackStack()
        }
    )
}

@Composable
fun ShoppingListScreen(
    state: ShoppingListUiState,
    onEvent: (ShoppingListEvent) -> Unit,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }

            Text(
                text = "Lista de compras",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        OutlinedTextField(
            value = state.search,
            onValueChange = { onEvent(ShoppingListEvent.OnSearchChange(it)) },
            label = { Text("Buscar") },
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {
            ShoppingListTable(
                state = state,
                onEvent = onEvent
            )
        }
    }
}

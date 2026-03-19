package com.litvy.litvysales.ui.purchases.purchase

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.purchases.PurchaseHomeUiState
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PurchaseHomeScreen(
    state: PurchaseHomeUiState,
    onRegisterPurchase: () -> Unit,
    onPurchaseHistory: () -> Unit,
    onShoppingList: () -> Unit,
    onPurchaseOrders: () -> Unit,
    onProviders: () -> Unit
) {
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(0.95f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PurchaseDashboardSummary(state)
                PurchaseQuickAccessGrid(
                    onRegisterPurchase = onRegisterPurchase,
                    onPurchaseHistory = onPurchaseHistory,
                    onShoppingList = onShoppingList,
                    onPurchaseOrders = onPurchaseOrders,
                    onProviders = onProviders
                )
            }

            Column(
                modifier = Modifier.weight(1.15f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PurchaseRecentActivity(state)
                PurchaseModuleHighlights(state)
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { PurchaseDashboardSummary(state) }
            item {
                PurchaseQuickAccessGrid(
                    onRegisterPurchase = onRegisterPurchase,
                    onPurchaseHistory = onPurchaseHistory,
                    onShoppingList = onShoppingList,
                    onPurchaseOrders = onPurchaseOrders,
                    onProviders = onProviders
                )
            }
            item { PurchaseRecentActivity(state) }
            item { PurchaseModuleHighlights(state) }
        }
    }
}

@Composable
private fun PurchaseDashboardSummary(state: PurchaseHomeUiState) {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "AR"))
    val lastPurchaseLabel = state.lastPurchaseTimestamp?.let(formatter::format) ?: "null"

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Compras",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Gestiona altas de proveedores, reabastecimiento, compras registradas y ordenes en un solo flujo.",
            style = MaterialTheme.typography.bodyMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("Pendientes", state.pendingOrders.toString(), Modifier.weight(1f))
            SummaryCard("Ordenes", "${state.activeOrders} activas", Modifier.weight(1f))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard("Visitas hoy", state.visitsToday.toString(), Modifier.weight(1f))
            SummaryCard("Ultima compra", lastPurchaseLabel, Modifier.weight(1f))
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            Text(text = value, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun PurchaseQuickAccessGrid(
    onRegisterPurchase: () -> Unit,
    onPurchaseHistory: () -> Unit,
    onShoppingList: () -> Unit,
    onPurchaseOrders: () -> Unit,
    onProviders: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PurchaseModuleCard(
                title = "Registrar compra",
                onClick = onRegisterPurchase,
                modifier = Modifier.weight(1f)
            )
            PurchaseModuleCard(
                title = "Historial compras",
                onClick = onPurchaseHistory,
                modifier = Modifier.weight(1f)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PurchaseModuleCard(
                title = "Lista de compras",
                onClick = onShoppingList,
                modifier = Modifier.weight(1f)
            )
            PurchaseModuleCard(
                title = "Ordenes de compra",
                onClick = onPurchaseOrders,
                modifier = Modifier.weight(1f)
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            PurchaseModuleCard(
                title = "Proveedores",
                onClick = onProviders,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PurchaseRecentActivity(state: PurchaseHomeUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Actividad reciente", style = MaterialTheme.typography.titleMedium)
            state.recentActivity.forEach { Text(it, style = MaterialTheme.typography.bodyMedium) }
        }
    }
}

@Composable
private fun PurchaseModuleHighlights(state: PurchaseHomeUiState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Alertas y seguimiento", style = MaterialTheme.typography.titleMedium)
            Text("Productos por reponer: null")
            Text("Ordenes pendientes: ${state.pendingOrders}")
            Text("Compras registradas: ${state.purchaseCount}")
        }
    }
}

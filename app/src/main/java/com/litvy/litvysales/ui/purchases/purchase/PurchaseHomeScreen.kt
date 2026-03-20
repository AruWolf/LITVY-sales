package com.litvy.litvysales.ui.purchases.purchase

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

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

    var showAlerts by remember{ mutableStateOf(false) }

    // PANTALLA ORIENTACION HORIZONTAL
    if (isLandscape) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            PurchaseHeader(
                onNotificationsClick = { showAlerts = true}
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // SECCION IZQUIERDA, BOTONERA
                Column(
                    modifier = Modifier
                        .weight(0.7f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PurchaseQuickAccessGrid(
                        onRegisterPurchase = onRegisterPurchase,
                        onPurchaseHistory = onPurchaseHistory,
                        onShoppingList = onShoppingList,
                        onPurchaseOrders = onPurchaseOrders,
                        onProviders = onProviders,
                        isLandscape = isLandscape
                    )
                }

                // SECCION DERECHA, RESUMEN INFORMATIVO
                Column(
                    modifier = Modifier
                        .weight(0.3f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PurchaseDashboardSummary(state)
                }
            }
        }
    } else // PANTALLA ORIENTACION VERTICAL
    {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { PurchaseHeader(
                onNotificationsClick = { showAlerts = true}
            ) }


            item {
                PurchaseQuickAccessGrid(
                    onRegisterPurchase = onRegisterPurchase,
                    onPurchaseHistory = onPurchaseHistory,
                    onShoppingList = onShoppingList,
                    onPurchaseOrders = onPurchaseOrders,
                    onProviders = onProviders,
                    isLandscape = isLandscape
                )
            }
            item { PurchaseDashboardSummary(state) }
        }
    }

    if (showAlerts) {
        PurchaseAlertsDialog(
            state = state,
            onDismiss = { showAlerts = false }
        )
    }
}

// ENCABEZADO DE PANTALLA
@Composable
private fun PurchaseHeader(
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Compras",
            style = MaterialTheme.typography.headlineMedium
        )

        IconButton(onClick = onNotificationsClick) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificaciones"
            )
        }
    }
}

// SECCIÓN DE INFORMACIÓN COMPLEMENTARIA
@Composable
private fun PurchaseDashboardSummary(state: PurchaseHomeUiState) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Resumen",
                style = MaterialTheme.typography.titleMedium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard("Pendientes", state.pendingOrders.toString(), Modifier.weight(1f))
                SummaryCard("Ordenes", "${state.activeOrders} activas", Modifier.weight(1f))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard("Visitas hoy", state.visitsToday.toString(), Modifier.weight(1f))
            }
        }
    }
}

// DISEÑO DE TARJETAS DE SECCIÓN COMPLEMENTARIA
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

// SECCIÓN DE BOTONERA PARA ACCESO A LAS FUNCIONALIDADES
@Composable
private fun PurchaseQuickAccessGrid(
    onRegisterPurchase: () -> Unit,
    onPurchaseHistory: () -> Unit,
    onShoppingList: () -> Unit,
    onPurchaseOrders: () -> Unit,
    onProviders: () -> Unit,
    isLandscape: Boolean
) {

    val mainCardHeight = if (isLandscape) 65.dp else 140.dp
    val secondaryCardHeight = if (isLandscape) 55.dp else 120.dp

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        PurchaseModuleCard(
            title = "Registrar compra",
            onClick = onRegisterPurchase,
            height = mainCardHeight,
            modifier = Modifier
                .fillMaxWidth(),
            icon = Icons.Default.AddShoppingCart,
            isLandscape = isLandscape
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PurchaseModuleCard(
                title = "Lista de compras",
                onClick = onShoppingList,
                height = secondaryCardHeight,
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Checklist,
                isLandscape = isLandscape
            )
            PurchaseModuleCard(
                title = "Ordenes de compra",
                onClick = onPurchaseOrders,
                height = secondaryCardHeight,
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Receipt,
                isLandscape = isLandscape
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PurchaseModuleCard(
                title = "Historial compras",
                onClick = onPurchaseHistory,
                height = secondaryCardHeight,
                modifier = Modifier.weight(1f),
                icon = Icons.Default.History,
                isLandscape = isLandscape
            )
            PurchaseModuleCard(
                title = "Proveedores",
                onClick = onProviders,
                height = secondaryCardHeight,
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Person,
                isLandscape = isLandscape
            )
        }
    }
}

// DIALOG DE ALERTAS
@Composable
private fun PurchaseAlertsDialog(
    state: PurchaseHomeUiState,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        },
        title = {
            Text("Alertas")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                if (state.pendingOrders > 0) {
                    Text("• Tenés ${state.pendingOrders} órdenes pendientes")
                }

                if (state.visitsToday > 0) {
                    Text("• ${state.visitsToday} proveedores visitan hoy")
                }

                if (state.purchaseCount == 0) {
                    Text("• No hay compras registradas")
                }

                if (
                    state.pendingOrders == 0 &&
                    state.visitsToday == 0 &&
                    state.purchaseCount > 0
                ) {
                    Text("Sin alertas por ahora")
                }
            }
        }
    )
}

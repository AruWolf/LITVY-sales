package com.litvy.litvysales.ui.purchases.purchase

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp


// Pantalla principal del modulo de compras
@Composable
fun PurchaseHomeScreen(
    onRegisterPurchase: () -> Unit,
    onShoppingList: () -> Unit,
    onPurchaseOrders: () -> Unit,
    onProviders: () -> Unit
) {

    // Validación para pantalla horizontal
    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Construcción del compose para orientación horizontal
    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Botón para ir a la pantalla de registro de compras
            PurchaseModuleCard(
                title = "Registrar compra",
                onClick = onRegisterPurchase,
                modifier = Modifier.weight(1f)
            )

            // Botón para ir a la pantalla de la lista de compras
            PurchaseModuleCard(
                title = "Lista de compras",
                onClick = onShoppingList,
                modifier = Modifier.weight(1f)
            )

            // Botón para ir a la pantalla de ordenes de compras
            PurchaseModuleCard(
                title = "Órdenes de compra",
                onClick = onPurchaseOrders,
                modifier = Modifier.weight(1f)
            )

            // Botón para ir a la pantalla de proveedores
            PurchaseModuleCard(
                title = "Proveedores",
                onClick = onProviders,
                modifier = Modifier.weight(1f)
            )
        }

        // Construcción del compose para orientación vertical
    } else {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            PurchaseModuleCard(
                title = "Registrar compra",
                onClick = onRegisterPurchase
            )

            PurchaseModuleCard(
                title = "Lista de compras",
                onClick = onShoppingList
            )

            PurchaseModuleCard(
                title = "Órdenes de compra",
                onClick = onPurchaseOrders
            )

            PurchaseModuleCard(
                title = "Proveedores",
                onClick = onProviders
            )
        }
    }
}
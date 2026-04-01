package com.litvy.litvysales.ui.purchases.purchaseOrder

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.ui.purchases.purchaseOrder.components.PurchaseOrderDetail
import com.litvy.litvysales.ui.purchases.purchaseOrder.components.PurchaseOrderList

@Composable
fun PurchaseOrderRoute(
    navController: NavController
) {
    val application = LocalContext.current.applicationContext as LitvySalesApplication
    val viewModel: PurchaseOrderViewModel = viewModel(
        factory = PurchaseOrderViewModel.PurchaseOrderViewModelFactory(application.container)
    )

    val state by viewModel.state.collectAsState()

    PurchaseOrderScreen(
        state = state,
        onEvent = { event ->
            if (event is PurchaseOrderEvent.OnCreateOrder) {
                navController.navigate("purchaseOrderCreate")
            } else {
                viewModel.onEvent(event)
            }
        },
        navController = navController
    )
}

@Composable
fun PurchaseOrderScreen(
    state: PurchaseOrderState,
    onEvent: (PurchaseOrderEvent) -> Unit,
    navController: NavController
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    LaunchedEffect(Unit) {
        val created = savedStateHandle?.get<Boolean>("order_created")

        if (created == true) {
            snackbarHostState.showSnackbar("Orden creada correctamente")
            savedStateHandle.remove<Boolean>("order_created")
        }
    }

    LaunchedEffect(state.selectedOrder?.status) {
        state.selectedOrder?.let {
            snackbarHostState.showSnackbar("Orden actualizada")
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }

                    Text(
                        "Órdenes de compra",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Button(onClick = { onEvent(PurchaseOrderEvent.OnCreateOrder) }) {
                    Text("Crear")
                }
            }
        }
    ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                PurchaseOrderList(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.weight(1f)
                )

                if (state.isDetailOpen && state.selectedOrder != null) {
                    Dialog(
                        onDismissRequest = {
                            onEvent(PurchaseOrderEvent.OnCloseDetail)
                        },
                        properties = DialogProperties(usePlatformDefaultWidth = false)
                    ) {
                        PurchaseOrderDetail(
                            state = state,
                            onEvent = onEvent
                        )
                    }
                }
            }
        }


}
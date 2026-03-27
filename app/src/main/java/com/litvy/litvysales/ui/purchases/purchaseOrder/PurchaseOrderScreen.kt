package com.litvy.litvysales.ui.purchases.purchaseOrder

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val snackbarHostState = remember { SnackbarHostState() }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    LaunchedEffect(Unit) {
        val created = savedStateHandle?.get<Boolean>("order_created")

        if (created == true) {
            snackbarHostState.showSnackbar("Orden creada correctamente")
            savedStateHandle.remove<Boolean>("order_created")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                        }
                    ) {
                        PurchaseOrderDetail(
                            state = state,
                            onEvent = onEvent
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        }
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

}
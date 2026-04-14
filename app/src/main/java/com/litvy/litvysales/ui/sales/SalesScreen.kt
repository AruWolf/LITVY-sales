package com.litvy.litvysales.ui.sales

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.ui.components.dialog.AddProductDialog
import com.litvy.litvysales.ui.sales.components.CartSection
import com.litvy.litvysales.ui.sales.components.EditItemDialog
import com.litvy.litvysales.ui.sales.components.FooterSection
import com.litvy.litvysales.ui.sales.components.PaymentSection

@Composable
fun SalesRoute() {
    val application =
        LocalContext.current.applicationContext as LitvySalesApplication

    val container = application.container

    val factory = remember {
        SalesViewModelFactory(
            createSaleUseCase = container.createSaleWithCashSessionUseCase,
            validateSaleUseCase = container.validateSaleUseCase,
            getPaymentMethodsUseCase = container.getPaymentMethodsUseCase,
            getProductsUseCase = container.getActiveProductsUseCase,
            getCategoriesUseCase = container.getCategoriesUseCase,
            getSubCategoriesByCategoryUseCase = container.getSubCategoriesByCategoryUseCase,
            getBrandBySubCategoryUseCase = container.getBrandBySubCategoryUseCase
        )
    }

    val viewModel: SalesViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsState()

    SalesScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun SalesScreen(
    state: SalesState,
    onEvent: (SalesEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.feedbackMessage) {
        val message = state.feedbackMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(
            message = message,
            duration = SnackbarDuration.Short
        )
        onEvent(SalesEvent.DismissFeedback)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Registrar venta",
                    style = MaterialTheme.typography.headlineSmall
                )

                Button(
                    onClick = { onEvent(SalesEvent.OpenAddProductDialog) }
                ) {
                    Text("Agregar producto")
                }
            }

            Spacer(Modifier.height(16.dp))

            CartSection(
                items = state.draft.items,
                itemsError = state.itemsError,
                modifier = Modifier.weight(1f),
                onEditItem = { onEvent(SalesEvent.OpenEditItemDialog(it)) },
                onDeleteItem = { onEvent(SalesEvent.DeleteItem(it)) }
            )

            Spacer(Modifier.height(16.dp))

            PaymentSection(
                paymentMethods = state.paymentMethods,
                selectedPaymentMethodId = state.selectedPaymentMethodId,
                paymentMethodError = state.paymentMethodError,
                onSelectPaymentMethod = { onEvent(SalesEvent.SelectPaymentMethod(it)) }
            )

            Spacer(Modifier.height(16.dp))

            FooterSection(
                subtotal = state.draft.subtotal,
                surcharge = state.draft.surcharge,
                total = state.draft.total,
                isSubmitting = state.isSubmitting,
                canConfirm = state.canConfirm,
                onConfirm = { onEvent(SalesEvent.ConfirmSale) }
            )
        }
    }

    if (state.showAddProductDialog) {
        AddProductDialog(
            state = state.addProductState,
            onEvent = { dialogEvent ->
                onEvent(SalesEvent.AddProductDialog(dialogEvent))
            }
        )
    }

    if (state.showEditItemDialog && state.editItemState != null) {
        EditItemDialog(
            state = state.editItemState,
            onQuantityChange = { onEvent(SalesEvent.UpdateEditItemQuantity(it)) },
            onUnitPriceChange = { onEvent(SalesEvent.UpdateEditItemUnitPrice(it)) },
            onSave = { onEvent(SalesEvent.SaveEditedItem) },
            onDelete = { onEvent(SalesEvent.DeleteItem(state.editItemState.productId)) },
            onDismiss = { onEvent(SalesEvent.CloseEditItemDialog) }
        )
    }
}

package com.litvy.litvysales.ui.systemparameters.paymentmethod

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.litvy.litvysales.ui.components.crud.CrudItemCard
import com.litvy.litvysales.ui.components.crud.CrudListScreen
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.ui.components.crud.CrudDialog
import com.litvy.litvysales.domain.model.util.PaymentMethod
import com.litvy.litvysales.domain.useCase.sales.paymentmethod.CreatePaymentMethodUseCase

@Composable
fun PaymentMethodRoute(
    navController: NavController
) {
    val application =
        LocalContext.current.applicationContext as LitvySalesApplication
    val container = application.container

    val factory = remember {
        PaymentMethodViewModelFactory(
            container.createPaymentMethodUseCase,
            container.getPaymentMethodsUseCase,
            container.updatePaymentMethodUseCase,
            container.deletePaymentMethodUseCase
        )
    }

    val viewModel: PaymentMethodViewModel = viewModel(factory = factory)
    val state = viewModel.state

    PaymentMethodScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = { navController.popBackStack() }
    )
}

// Metodo para generar
@Composable
fun PaymentMethodScreen(
    state: PaymentMethodState,
    onEvent: (PaymentMethodEvent) -> Unit,
    onBack: () -> Unit,
) {

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    CrudListScreen(
        title = "Métodos de pago",
        items = state.items,
        onCreate = { onEvent(PaymentMethodEvent.OnCreateClick) },
        onBack = onBack
    ) { item: PaymentMethod ->

        CrudItemCard(
            title = item.name,
            subtitle = "Recargo: ${item.surchargePercentage}%",
            onEdit = { onEvent(PaymentMethodEvent.OnEdit(item)) },
            onDelete = { onEvent(PaymentMethodEvent.OnDelete(item)) }
        )
    }

    if (state.showDialog) {

        CrudDialog(
            title = if (state.editingItem == null) "Nuevo método" else "Editar método",
            isLandscape = isLandscape,
            onConfirm = { onEvent(PaymentMethodEvent.OnSave) },
            onDismiss = { onEvent(PaymentMethodEvent.OnDismissDialog) }
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(PaymentMethodEvent.OnNameChange(it))
                    },
                    label = { Text("Nombre") },
                    isError = state.errors.containsKey("name"),
                    supportingText = {
                        state.errors["name"]?.let { Text(it) }
                    }
                )

                OutlinedTextField(
                    value = state.surcharge,
                    onValueChange = {
                        onEvent(PaymentMethodEvent.OnSurchargeChange(it))
                    },
                    label = { Text("Recargo (%)") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.errors.containsKey("surchargePercentage"),
                    supportingText = {
                        state.errors["surchargePercentage"]?.let { Text(it) }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
    }
}
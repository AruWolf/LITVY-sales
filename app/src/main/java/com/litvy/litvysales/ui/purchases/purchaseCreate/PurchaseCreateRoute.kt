package com.litvy.litvysales.ui.purchases.purchaseCreate

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PurchaseCreateRoute() {

    val viewModel: PurchaseCreateViewModel = viewModel()

    val state by viewModel.state.collectAsState()

    PurchaseCreateScreen(
        state = state,
        onEvent = viewModel::onEvent
    )

}
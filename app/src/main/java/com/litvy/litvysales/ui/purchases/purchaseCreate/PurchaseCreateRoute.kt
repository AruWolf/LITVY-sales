package com.litvy.litvysales.ui.purchases.purchaseCreate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.litvy.litvysales.LitvySalesApplication

@Composable
fun PurchaseCreateRoute(
    navController: NavController
) {
    val application =
        LocalContext.current.applicationContext as LitvySalesApplication
    val container = application.container

    val factory = remember {
        PurchaseCreateViewModelFactory(
            getProvidersWithVisitDaysUseCase = container.getProvidersWithVisitDaysUseCase,
            getInvoiceTypesUseCase = container.getInvoiceTypesUseCase,
            getPaymentMethodsUseCase = container.getPaymentMethodsUseCase,
            getCategoriesUseCase = container.getCategoriesUseCase,
            getSubCategoriesByCategoryUseCase = container.getSubCategoriesByCategoryUseCase,
            getBrandBySubCategoryUseCase = container.getBrandBySubCategoryUseCase,
            getActiveProductsUseCase = container.getActiveProductsUseCase,
            getPurchaseOrdersUseCase = container.getPurchaseOrdersUseCase,
            getPurchaseOrderItemsUseCase = container.getPurchaseOrderItemsUseCase,
            registerPurchaseUseCase = container.registerPurchaseUseCase,
            updatePurchaseOrderUseCase = container.updatePurchaseOrderUseCase
        )
    }

    val viewModel: PurchaseCreateViewModel = viewModel(factory = factory)

    val state by viewModel.state.collectAsStateWithLifecycle()

    PurchaseCreateScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = { navController.popBackStack() }
    )

}

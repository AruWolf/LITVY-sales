package com.litvy.litvysales.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.litvy.litvysales.ui.catalog.CatalogScreen
import com.litvy.litvysales.ui.sales.SalesScreen
import com.litvy.litvysales.ui.purchases.PurchaseScreen
import com.litvy.litvysales.ui.purchases.purchaseCreate.PurchaseCreateRoute
import com.litvy.litvysales.ui.purchases.provider.ProviderRoute
import com.litvy.litvysales.ui.purchases.purchaseHistory.PurchaseHistoryScreen
import com.litvy.litvysales.ui.purchases.purchaseOrder.PurchaseOrderRoute
import com.litvy.litvysales.ui.purchases.purchaseOrder.create.PurchaseOrderCreateRoute
import com.litvy.litvysales.ui.purchases.shoppingList.ShoppingListScreen
import com.litvy.litvysales.ui.purchases.shoppingList.ShoppingListScreenRoute
import com.litvy.litvysales.ui.sales.SalesRoute
import com.litvy.litvysales.ui.systemparameters.ParameterScreen
import com.litvy.litvysales.ui.systemparameters.paymentmethod.PaymentMethodRoute
import com.litvy.litvysales.ui.systemparameters.paymentmethod.PaymentMethodScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = "sales",
        modifier = modifier
    ) {

        composable("sales") {
            SalesRoute()
        }

        composable("catalog") {
            CatalogScreen()
        }

        composable("purchases") {
            PurchaseScreen(navController)
        }

        composable("purchaseCreate") {
            PurchaseCreateRoute(navController)
        }

        composable("purchaseHistory") {
            PurchaseHistoryScreen()
        }

        composable("shoppingList") {
            ShoppingListScreenRoute(navController)
        }

        composable("purchaseOrders") {
            PurchaseOrderRoute(navController)
        }

        composable("purchaseOrderCreate"){
            PurchaseOrderCreateRoute(navController)
        }

        composable("providers") {
            ProviderRoute(navController)
        }

        composable("parameters") {
            ParameterScreen(navController)
        }

        composable("paymentMethods") {
            PaymentMethodRoute(navController)
        }

    }

}

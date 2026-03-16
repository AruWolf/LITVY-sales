package com.litvy.litvysales.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.litvy.litvysales.ui.catalog.CatalogScreen
import com.litvy.litvysales.ui.sales.SalesScreen
import com.litvy.litvysales.ui.purchases.PurchaseScreen
import com.litvy.litvysales.ui.purchases.purchaseCreate.PurchaseCreateRoute

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
            SalesScreen()
        }

        composable("catalog") {
            CatalogScreen()
        }

        composable("purchases") {
            PurchaseScreen(navController)
        }

        composable("purchaseCreate") {
            PurchaseCreateRoute()
        }

    }

}
package com.litvy.litvysales.ui.purchases

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.litvy.litvysales.ui.purchases.purchase.PurchaseHomeScreen

@Composable
fun PurchaseScreen(
    navController: NavController
) {

    PurchaseHomeScreen(

        onRegisterPurchase = {
            navController.navigate("purchaseCreate")
        },

        onShoppingList = { },

        onPurchaseOrders = { },

        onProviders = { }

    )

}
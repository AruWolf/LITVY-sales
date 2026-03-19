package com.litvy.litvysales.ui.purchases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.ui.purchases.purchase.PurchaseHomeScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import java.util.Calendar

@Composable
fun PurchaseScreen(
    navController: NavController
) {
    val application = LocalContext.current.applicationContext as LitvySalesApplication
    val container = application.container

    var homeState by remember { mutableStateOf(PurchaseHomeUiState()) }

    LaunchedEffect(container) {
        val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        val purchasesFlow = container.getPurchasesUseCase()

        combine(
            purchasesFlow,
            container.getPurchaseOrdersUseCase(),
            container.getProvidersWithVisitDaysUseCase()
        ) { purchases, orders, providers ->
            val pendingOrders = orders.count { it.status.name == "PENDING" }
            val activeOrders = orders.count { order ->
                order.status.name == "PENDING" || order.status.name == "SENT"
            }
            val visitsToday = providers.count { provider ->
                provider.visitDays.contains(currentDayOfWeek)
            }
            val lastPurchase = purchases.maxByOrNull { it.createdAt }

            PurchaseHomeUiState(
                pendingOrders = pendingOrders,
                activeOrders = activeOrders,
                visitsToday = visitsToday,
                purchaseCount = purchases.size,
                lastPurchaseTimestamp = lastPurchase?.createdAt,
                recentActivity = buildList {
                    lastPurchase?.let {
                        add("Ultima compra registrada: #${it.id}")
                    }
                    orders
                        .sortedByDescending { it.createdAt }
                        .take(2)
                        .forEach { order ->
                            add("Orden #${order.id} en estado ${order.status.name}")
                        }
                    if (isEmpty()) {
                        add("Sin actividad reciente")
                    }
                }
            )
        }.collect { state ->
            homeState = state
        }
    }

    PurchaseHomeScreen(
        state = homeState,

        onRegisterPurchase = {
            navController.navigate("purchaseCreate")
        },

        onPurchaseHistory = {
            navController.navigate("purchaseHistory")
        },

        onShoppingList = {
            navController.navigate("shoppingList")
        },

        onPurchaseOrders = {
            navController.navigate("purchaseOrders")
        },

        onProviders = {
            navController.navigate("providers")
        }

    )

}

data class PurchaseHomeUiState(
    val pendingOrders: Int = 0,
    val activeOrders: Int = 0,
    val visitsToday: Int = 0,
    val purchaseCount: Int = 0,
    val lastPurchaseTimestamp: Long? = null,
    val recentActivity: List<String> = listOf("Sin actividad reciente")
)

package com.litvy.litvysales.ui.purchases.purchaseOrder

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.domain.model.enums.PurchaseOrderStatus
import com.litvy.litvysales.domain.model.purchases.PurchaseOrder
import com.litvy.litvysales.domain.model.purchases.PurchaseOrderItem
import com.litvy.litvysales.ui.util.model.PurchaseOrderItemUi
import com.litvy.litvysales.ui.util.model.PurchaseOrderUi
import com.litvy.litvysales.util.MoneyFormatter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PurchaseOrderRoute(
    navController: NavController
) {
    val application = LocalContext.current.applicationContext as LitvySalesApplication
    val container = application.container
    val scope = rememberCoroutineScope()

    var orderState by remember { mutableStateOf(PurchaseOrderScreenState()) }
    var selectedStatus by rememberSaveable { mutableStateOf("ALL") }
    var search by rememberSaveable { mutableStateOf("") }
    var selectedOrderId by rememberSaveable { mutableStateOf<Int?>(null) }
    var feedback by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(container) {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("es", "AR"))
        val providers = container.getProvidersWithVisitDaysUseCase().first().associateBy { it.provider.id }
        val products = container.getActiveProductsUseCase().first().associateBy { it.id }

        container.getPurchaseOrdersUseCase().collect { orders ->
            val orderItems = mutableMapOf<Int, List<PurchaseOrderItem>>()
            orders.forEach { order ->
                orderItems[order.id] = container.getPurchaseOrderItemsUseCase(order.id).first()
            }

            orderState = PurchaseOrderScreenState(
                orders = orders,
                orderItems = orderItems,
                uiOrders = orders.map { order ->
                    PurchaseOrderUi(
                        id = order.id,
                        providerId = order.providerId,
                        providerName = providers[order.providerId]?.provider?.name ?: "Proveedor #${order.providerId}",
                        status = order.status.name,
                        expectedDeliveryLabel = order.expectedDeliveryDate?.let { formatter.format(it) },
                        items = orderItems[order.id].orEmpty().map { item ->
                            PurchaseOrderItemUi(
                                productId = item.productId,
                                productName = products[item.productId]?.name ?: "Producto #${item.productId}",
                                quantity = item.quantity,
                                suggestedUnitPriceInCents = products[item.productId]?.purchasePriceInCents
                            )
                        }
                    )
                }
            )

            if (selectedOrderId == null) {
                selectedOrderId = orders.firstOrNull()?.id
            }
        }
    }

    val filtered = orderState.uiOrders.filter { order ->
        (selectedStatus == "ALL" || order.status == selectedStatus) &&
            (
                search.isBlank() ||
                    order.providerName.contains(search, ignoreCase = true) ||
                    order.id.toString().contains(search)
                )
    }
    val selected = filtered.firstOrNull { it.id == selectedOrderId } ?: filtered.firstOrNull()
    val selectedDomainOrder = selected?.id?.let { orderState.orders.firstOrNull { order -> order.id == it } }
    val selectedDomainItems = selected?.id?.let { orderState.orderItems[it].orEmpty() }.orEmpty()

    fun updateStatus(targetStatus: PurchaseOrderStatus) {
        val order = selectedDomainOrder ?: return
        if (selectedDomainItems.isEmpty()) return

        scope.launch {
            val result = container.updatePurchaseOrderUseCase(
                purchaseOrder = order.copy(status = targetStatus),
                items = selectedDomainItems
            )

            feedback = if (result is com.litvy.litvysales.domain.validation.ValidationResult.Success) {
                "Orden #${order.id} actualizada a ${targetStatus.name}."
            } else {
                "No se pudo actualizar la orden seleccionada."
            }
        }
    }

    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PurchaseOrderListPane(
                orders = filtered,
                selectedStatus = selectedStatus,
                search = search,
                selectedOrderId = selected?.id,
                onStatusChange = { selectedStatus = it },
                onSearchChange = { search = it },
                onSelect = { selectedOrderId = it.id },
                modifier = Modifier.weight(0.95f)
            )
            PurchaseOrderDetailPane(
                order = selected,
                feedback = feedback,
                onMarkSent = { updateStatus(PurchaseOrderStatus.SENT) },
                onMarkReceived = { updateStatus(PurchaseOrderStatus.RECEIVED) },
                onRegisterPurchase = { navController.navigate("purchaseCreate") },
                modifier = Modifier.weight(1.05f)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PurchaseOrderListPane(
                orders = filtered,
                selectedStatus = selectedStatus,
                search = search,
                selectedOrderId = selected?.id,
                onStatusChange = { selectedStatus = it },
                onSearchChange = { search = it },
                onSelect = { selectedOrderId = it.id },
                modifier = Modifier.weight(1f)
            )
            PurchaseOrderDetailPane(
                order = selected,
                feedback = feedback,
                onMarkSent = { updateStatus(PurchaseOrderStatus.SENT) },
                onMarkReceived = { updateStatus(PurchaseOrderStatus.RECEIVED) },
                onRegisterPurchase = { navController.navigate("purchaseCreate") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PurchaseOrderListPane(
    orders: List<PurchaseOrderUi>,
    selectedStatus: String,
    search: String,
    selectedOrderId: Int?,
    onStatusChange: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onSelect: (PurchaseOrderUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxHeight()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Ordenes de compra", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = search,
                onValueChange = onSearchChange,
                label = { Text("Buscar orden o proveedor") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("ALL", "PENDING", "SENT", "RECEIVED", "CANCELLED").forEach { status ->
                    FilterChip(
                        selected = selectedStatus == status,
                        onClick = { onStatusChange(status) },
                        label = { Text(if (status == "ALL") "Todas" else status) }
                    )
                }
            }

            if (orders.isEmpty()) {
                Text("No hay ordenes disponibles para los filtros elegidos.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(orders, key = { it.id }) { order ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelect(order) }
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "#${order.id} - ${order.providerName}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("Estado: ${order.status}")
                                order.expectedDeliveryLabel?.let { Text("Entrega: $it") }
                                Text("Items: ${order.items.size}")
                                if (selectedOrderId == order.id) {
                                    Text("Seleccionada", style = MaterialTheme.typography.labelLarge)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PurchaseOrderDetailPane(
    order: PurchaseOrderUi?,
    feedback: String?,
    onMarkSent: () -> Unit,
    onMarkReceived: () -> Unit,
    onRegisterPurchase: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxHeight()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Detalle de orden", style = MaterialTheme.typography.headlineSmall)

            if (order == null) {
                Text("No hay una orden seleccionada.")
                return@Column
            }

            Text("Proveedor: ${order.providerName}")
            Text("Estado: ${order.status}")
            order.expectedDeliveryLabel?.let { Text("Entrega estimada: $it") }

            Text("Productos solicitados", style = MaterialTheme.typography.titleMedium)
            if (order.items.isEmpty()) {
                Text("La orden no tiene items cargados.")
            } else {
                order.items.forEach { item ->
                    val suggestedPrice = item.suggestedUnitPriceInCents?.let(MoneyFormatter::formatFromCents)
                        ?: "Sin sugerencia"
                    Text("${item.productName} - ${item.quantity} uds - sugerido $suggestedPrice")
                }
            }

            feedback?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (order.status == PurchaseOrderStatus.PENDING.name) {
                    Button(onClick = onMarkSent) { Text("Marcar enviada") }
                }
                if (order.status != PurchaseOrderStatus.RECEIVED.name &&
                    order.status != PurchaseOrderStatus.CANCELLED.name
                ) {
                    Button(onClick = onMarkReceived) { Text("Marcar recibida") }
                }
                Button(onClick = onRegisterPurchase) { Text("Registrar compra") }
            }
        }
    }
}

private data class PurchaseOrderScreenState(
    val orders: List<PurchaseOrder> = emptyList(),
    val orderItems: Map<Int, List<PurchaseOrderItem>> = emptyMap(),
    val uiOrders: List<PurchaseOrderUi> = emptyList()
)

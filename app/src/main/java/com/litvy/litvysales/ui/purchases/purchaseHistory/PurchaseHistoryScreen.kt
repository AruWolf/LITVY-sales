package com.litvy.litvysales.ui.purchases.purchaseHistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.util.MoneyFormatter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PurchaseHistoryScreen() {
    val application = LocalContext.current.applicationContext as LitvySalesApplication
    val container = application.container

    var purchases by remember { mutableStateOf<List<PurchaseHistoryItem>>(emptyList()) }
    var search by remember { mutableStateOf("") }

    LaunchedEffect(container) {
        val providers = container.getProvidersWithVisitDaysUseCase().first()
            .associateBy { it.provider.id }
        val invoiceTypes = container.getInvoiceTypesUseCase().associateBy { it.id }
        val paymentMethods = container.getPaymentMethodsUseCase().associateBy { it.id }
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("es", "AR"))

        container.getPurchasesUseCase().collect { purchaseList ->
            purchases = purchaseList.map { purchase ->
                PurchaseHistoryItem(
                    id = purchase.id,
                    provider = providers[purchase.providerId]?.provider?.name ?: "Proveedor #${purchase.providerId}",
                    invoiceType = invoiceTypes[purchase.invoiceTypeId]?.code ?: "Sin factura",
                    paymentMethod = paymentMethods[purchase.paymentMethodId]?.name ?: "Sin metodo",
                    salesRep = purchase.salesRepName?.takeIf { it.isNotBlank() } ?: "No informado",
                    date = formatter.format(purchase.createdAt),
                    total = MoneyFormatter.formatFromCents(purchase.totalInCents)
                )
            }
        }
    }

    val filtered = purchases.filter {
        search.isBlank() ||
            it.provider.contains(search, ignoreCase = true) ||
            it.id.toString().contains(search) ||
            it.salesRep.contains(search, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Historial de compras", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            label = { Text("Buscar compra, proveedor o preventista") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (filtered.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Todavia no hay compras registradas.",
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filtered, key = { it.id }) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("#${item.id} - ${item.provider}", style = MaterialTheme.typography.titleMedium)
                            Text("Fecha: ${item.date}")
                            Text("Factura: ${item.invoiceType} - Pago: ${item.paymentMethod}")
                            Text("Preventista: ${item.salesRep}")
                            Text("Total: ${item.total}")
                        }
                    }
                }
            }
        }
    }
}

private data class PurchaseHistoryItem(
    val id: Int,
    val provider: String,
    val invoiceType: String,
    val paymentMethod: String,
    val salesRep: String,
    val date: String,
    val total: String
)

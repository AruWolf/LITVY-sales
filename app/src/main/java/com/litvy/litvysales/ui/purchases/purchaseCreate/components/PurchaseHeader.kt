package com.litvy.litvysales.ui.purchases.purchaseCreate.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.purchases.purchaseCreate.PurchaseCreateEvent
import com.litvy.litvysales.ui.purchases.purchaseCreate.PurchaseCreateState

@Composable
fun PurchaseHeader(
    state: PurchaseCreateState,
    onEvent: (PurchaseCreateEvent) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedTextField(
            value = state.provider?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Proveedor") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        onEvent(PurchaseCreateEvent.OpenProviderSelector)
                    }                ) {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = state.invoiceType,
                onValueChange = {
                    onEvent(
                        PurchaseCreateEvent.SelectInvoiceType(it)
                    )
                },
                label = { Text("Tipo factura") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = state.paymentMethod,
                onValueChange = {
                    onEvent(
                        PurchaseCreateEvent.SelectPaymentMethod(it)
                    )
                },
                label = { Text("Método pago") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
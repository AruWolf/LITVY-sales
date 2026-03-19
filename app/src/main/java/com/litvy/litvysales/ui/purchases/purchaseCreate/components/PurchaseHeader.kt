package com.litvy.litvysales.ui.purchases.purchaseCreate.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
        DropdownSelector(
            label = "Proveedor",
            value = state.provider?.name.orEmpty(),
            options = state.providers.map { it.name },
            error = state.providerError,
            onOptionSelected = { selectedName ->
                state.providers.firstOrNull { it.name == selectedName }?.let { provider ->
                    onEvent(PurchaseCreateEvent.SelectProvider(provider))
                }
            }
        )

        OutlinedTextField(
            value = state.salesRepName,
            onValueChange = { onEvent(PurchaseCreateEvent.UpdateSalesRepName(it)) },
            label = { Text("Preventista / vendedor") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.salesRepError != null,
            singleLine = true
        )

        state.salesRepError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DropdownSelector(
                label = "Tipo factura",
                value = state.invoiceType,
                options = state.invoiceTypeOptions,
                error = state.invoiceTypeError,
                modifier = Modifier.weight(1f),
                onOptionSelected = { onEvent(PurchaseCreateEvent.SelectInvoiceType(it)) }
            )

            DropdownSelector(
                label = "Metodo pago",
                value = state.paymentMethod,
                options = state.paymentMethodOptions,
                error = state.paymentMethodError,
                modifier = Modifier.weight(1f),
                onOptionSelected = { onEvent(PurchaseCreateEvent.SelectPaymentMethod(it)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DropdownSelector(
    label: String,
    value: String,
    options: List<String>,
    error: String?,
    modifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            expanded = false
                            onOptionSelected(option)
                        }
                    )
                }
            }
        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

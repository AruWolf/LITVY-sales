package com.litvy.litvysales.ui.sales.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.litvy.litvysales.domain.model.util.PaymentMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSection(
    paymentMethods: List<PaymentMethod>,
    selectedPaymentMethodId: Int?,
    paymentMethodError: String?,
    onSelectPaymentMethod: (PaymentMethod) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedMethod = paymentMethods.firstOrNull { it.id == selectedPaymentMethodId }

    Column {
        Text(
            text = "Pago",
            style = MaterialTheme.typography.titleLarge
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.padding(top = 12.dp)
        ) {
            OutlinedTextField(
                value = selectedMethod?.name.orEmpty(),
                onValueChange = {},
                readOnly = true,
                label = { Text("Metodo de pago") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                isError = paymentMethodError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                paymentMethods.forEach { method ->
                    DropdownMenuItem(
                        text = {
                            val surcharge = if (method.surchargePercentage > 0.0) {
                                " (+${method.surchargePercentage}%)"
                            } else {
                                ""
                            }
                            Text("${method.name}$surcharge")
                        },
                        onClick = {
                            expanded = false
                            onSelectPaymentMethod(method)
                        }
                    )
                }
            }
        }

        when {
            paymentMethodError != null -> {
                Text(
                    text = paymentMethodError,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            selectedMethod != null && selectedMethod.surchargePercentage > 0.0 -> {
                Text(
                    text = "Recargo aplicado: ${selectedMethod.surchargePercentage}%",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

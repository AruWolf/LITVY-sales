package com.litvy.litvysales.ui.sales.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.util.MoneyFormatter

@Composable
fun FooterSection(
    subtotal: Long,
    surcharge: Long,
    total: Long,
    isSubmitting: Boolean,
    canConfirm: Boolean,
    onConfirm: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Subtotal: ${MoneyFormatter.formatFromCents(subtotal)}",
            style = MaterialTheme.typography.bodyLarge
        )

        if (surcharge > 0L) {
            Text(
                text = "Recargo: ${MoneyFormatter.formatFromCents(surcharge)}",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Text(
            text = "Total: ${MoneyFormatter.formatFromCents(total)}",
            style = MaterialTheme.typography.titleLarge
        )

        Button(
            onClick = onConfirm,
            enabled = canConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(if (isSubmitting) "Confirmando..." else "Confirmar venta")
        }
    }
}

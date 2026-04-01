package com.litvy.litvysales.ui.purchases.purchaseCreate.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.purchases.purchaseCreate.PurchaseCreateEvent

@Composable
fun PurchaseOrderConflictDialog(
    onEvent: (PurchaseCreateEvent) -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onEvent(PurchaseCreateEvent.DismissPurchaseOrderConflict)
        },
        title = {
            Text("Cargar orden de compra")
        },
        text = {
            Text("Ya cargaste productos manualmente.\n\n¿Querés fusionarlos o reemplazarlos?")
        },
        confirmButton = {
            Button(
                onClick = {
                    onEvent(PurchaseCreateEvent.ApplyPurchaseOrderByMerge)
                }
            ) {
                Text("Fusionar")
            }
        },
        dismissButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onEvent(PurchaseCreateEvent.ApplyPurchaseOrderByOverwrite)
                    }
                ) {
                    Text("Sobreescribir")
                }

                OutlinedButton(
                    onClick = {
                        onEvent(PurchaseCreateEvent.DismissPurchaseOrderConflict)
                    }
                ) {
                    Text("Cancelar")
                }
            }
        }
    )
}
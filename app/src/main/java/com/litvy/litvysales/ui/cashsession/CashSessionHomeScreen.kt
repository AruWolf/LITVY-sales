package com.litvy.litvysales.ui.cashsession

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.litvy.litvysales.ui.purchases.purchase.PurchaseModuleCard

@Composable
fun CashSessionHomeScreen(
    navController: NavController
) {
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Caja",
            style = MaterialTheme.typography.headlineMedium
        )

        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PurchaseModuleCard(
                    title = "Sesiones de caja",
                    onClick = { navController.navigate("cashSessions/list") },
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Wallet,
                    isLandscape = true
                )
                PurchaseModuleCard(
                    title = "Configuración automática",
                    onClick = { navController.navigate("cashSessions/settings") },
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Schedule,
                    isLandscape = true
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    Text(
                        text = "Consultá sesiones por fecha, cerrá cajas activas y configurá franjas automáticas por día.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            PurchaseModuleCard(
                title = "Sesiones de caja",
                onClick = { navController.navigate("cashSessions/list") },
                height = 120.dp,
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Wallet,
                isLandscape = false
            )
            PurchaseModuleCard(
                title = "Configuración automática",
                onClick = { navController.navigate("cashSessions/settings") },
                height = 120.dp,
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Schedule,
                isLandscape = false
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = null)
                        Text(
                            text = "Operación de caja",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Text(
                        text = "Entrá a sesiones para abrir, cerrar y revisar movimientos asociados a cada jornada.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

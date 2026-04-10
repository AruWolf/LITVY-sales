package com.litvy.litvysales.ui.systemparameters

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ParameterScreen(
    navController: NavController
) {

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val columns = if (isLandscape) 4 else 2

    // Instanciación de los items de configuación.
    val items = listOf(
        ParameterItem("Métodos de pago", Icons.Default.Payments, "paymentMethods"),
        ParameterItem("Tipos de factura", Icons.Default.Receipt, "invoiceTypes"),
        ParameterItem("Impuestos", Icons.Default.Percent, "taxes")
    )

    // Composable general de pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Titulo de pantalla ParameterScreen
        Text(
            text = "Parámetros del sistema",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Composable para disponer los elementos card de manera ordenada y en base al tamaño de pantalla
        LazyVerticalGrid(
            columns = GridCells.Fixed(columns),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            // Recorrido de cada item instanciado para la configuración
            items(items) { item ->

                ParameterCard(
                    item = item,
                    onClick = {
                        navController.navigate(item.route)
                    }
                )

            }

        }

    }
}

//  Metodo composable que genera los parametros disponibles para configurar
@Composable
fun ParameterCard(
    item: ParameterItem,
    onClick: () -> Unit
) {

    // Composable tipo tarjeta que representa cada configuración
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2.2f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge
                )

            }

        }

    }
}

// Elementos de cada item de configuración
data class ParameterItem(
    val title: String, // Titulo que se utiliza en la card
    val icon: ImageVector, // Icono seleccionado para utilizar en la card
    val route: String // Ruta de acceso a la pantalla de configuración correspondiente
)
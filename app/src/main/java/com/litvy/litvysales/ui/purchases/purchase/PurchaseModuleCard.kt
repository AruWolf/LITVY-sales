package com.litvy.litvysales.ui.purchases.purchase

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Botón del tipo tarjeta
@Composable
fun PurchaseModuleCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 120.dp,
    icon: ImageVector? = null,
    isLandscape: Boolean
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

            // ORIENTACION HORIZONTAL
            if (isLandscape) {

                Row(
                    modifier = Modifier
                        .align(Alignment.Center),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            } else { // ORIENTACION VERTICAL

                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(28.dp)
                    )
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
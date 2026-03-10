package com.litvy.litvysales.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration

// Metodo generico para ruteo del tipo BreadCrumb. Ejemplo (Carpeta>SubCarpeta>Archivo)
@Composable
fun Breadcrumb(
    items: List<BreadcrumbItem>,
    modifier: Modifier = Modifier
) {

    val configuration = LocalConfiguration.current
    val isPortrait =
        configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    Row(
        modifier = modifier.then(
            if (isPortrait)
                Modifier.horizontalScroll(rememberScrollState())
            else
                Modifier
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        items.forEachIndexed { index, item ->

            if (item.onClick != null) {

                Text(
                    text = item.label,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        item.onClick.invoke()
                    }
                )

            } else {

                Text(
                    text = item.label
                )

            }

            if (index < items.lastIndex) {
                Text(" > ")
            }

        }

    }
}
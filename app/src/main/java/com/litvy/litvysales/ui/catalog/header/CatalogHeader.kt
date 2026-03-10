package com.litvy.litvysales.ui.catalog.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.ui.components.Breadcrumb
import com.litvy.litvysales.ui.components.BreadcrumbItem

@Composable
fun CatalogHeader(
    breadcrumb: List<BreadcrumbItem>,
    title: String,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onCreate: () -> Unit,
    onSearch: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (canGoBack) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, "Volver")
            }
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {

            if (breadcrumb.isNotEmpty()) {
                Breadcrumb(breadcrumb)
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

        }

        IconButton(onClick = onSearch) {
            Icon(Icons.Default.Search, "Buscar")
        }

        Spacer(Modifier.width(8.dp))

        Button(onClick = onCreate) {
            Text("+ Crear")
        }

    }

}
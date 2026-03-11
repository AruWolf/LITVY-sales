package com.litvy.litvysales.ui.catalog.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Diseño de grilla para contexto catalogo sobre los niveles -Categoria -SubCategoria y -Marcas
@Composable
fun CatalogGrid(
    items: List<Pair<Int,String>>,
    onItemClick: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onInspect: (Int) -> Unit
) {

    // Grilla en orientación vertical
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp), // Tamaño de columnas.
        verticalArrangement = Arrangement.spacedBy(12.dp), // Espaciado de los elementos verticalmente
        horizontalArrangement = Arrangement.spacedBy(12.dp) // Espaciado de los elementos horizontalmente
    ) {

        items(items) { item -> // Items que se van a construir en la grilla

            // Propiedades de cada elemento de la grilla
            CatalogCard(
                id = item.first, // Asignación de numero identificador(id)
                name = item.second, // Nombre que figura en la carta
                onClick = onItemClick, // Evento de click sobre el elemento
                onEdit = onEdit, // Evento de edición
                onInspect = onInspect // Evento de inspección
            )

        }

    }

}

// Diseño de cada elemento dentro de la grilla
@Composable
fun CatalogCard(
    id: Int,
    name: String,
    onClick: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onInspect: (Int) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth() // Rellena el tamaño maximo horizontalmente posible
            .height(80.dp), // Altura de cada elemento de la grilla
        onClick = { onClick(id) }
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            ItemMenu(
                id = id,
                onEdit = onEdit,
                onInspect = onInspect
            )

            Spacer(Modifier.width(12.dp))

            // Compose del nombre del item
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium
            )

        }

    }

}

// Diseño de icono de opciones con menu contextual, para cada elemento de la grilla
@Composable
fun ItemMenu(
    id: Int,
    onEdit: (Int) -> Unit,
    onInspect: (Int) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    Box {

        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Opciones") // Icono de configuración
        }

        // Lista desplegable de opciones
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            // Item de edita
            DropdownMenuItem(
                text = { Text("Editar") },
                onClick = {
                    expanded = false
                    onEdit(id)
                }
            )

            // Item de ver datos
            DropdownMenuItem(
                text = { Text("Ver datos") },
                onClick = {
                    expanded = false
                    onInspect(id)
                }
            )

        }

    }

}
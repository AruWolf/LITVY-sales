package com.litvy.litvysales.ui.purchases.provider.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.litvy.litvysales.domain.filter.purchases.ProviderSortBy
import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays
import com.litvy.litvysales.ui.purchases.provider.*
import com.litvy.litvysales.ui.util.SortIcon

// Panel de lista de proveedores
@Composable
fun ProviderListPane(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    // Contenedor del panel
    Card(modifier = modifier.fillMaxHeight()) {
        // Columna de elementos dentro del contenedor
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Fila de elementos del encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Boton back para volver a la ventana previa(PurchaseHomeScreen)
                IconButton(
                    onClick = { onEvent(ProviderEvent.OnBack) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Titulo del card contenedor
                Text(
                    text = "Proveedores",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.weight(1f))

                // Botón para crear un nuevo proveedor
                Button(
                    onClick = { onEvent(ProviderEvent.OnAddNew) } // Abre el dialog para crear un nuevo proveedor
                ) {
                    Text("Nuevo proveedor")
                }
            }

            // Columna que contiene la lista de proveedores existentes en sistema. Permite scrolleo
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Genera una un contenedor para cada uno de los proveedores y sus datos
                items(state.providers, key = { it.provider.id ?: 0 }) { provider ->
                    ProviderRow(
                        provider = provider,
                        selected = provider.provider.id == state.selectedProvider?.provider?.id,
                        onClick = { // Abre el dialog para ver/editar los datos de un proveedor de la lista
                            onEvent(ProviderEvent.OnProviderSelected(provider.provider))
                        }
                    )
                }
            }
        }
    }
}

// Generación de vista Vertical de la lista
@Composable
fun ProviderListPaneVertical(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Contenedor de los elementos de la lista
    Card(modifier = modifier.fillMaxHeight()) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Fila de elementos del encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón de retroceso
                IconButton(
                    onClick = { onEvent(ProviderEvent.OnBack) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Titulo
                Text(
                    text = "Proveedores",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            // Buscador
            OutlinedTextField(
                value = state.search,
                onValueChange = { onEvent(ProviderEvent.OnSearchChange(it)) },
                label = { Text("Buscar proveedor") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Botón para crear un nuevo proveedor
            Button(
                onClick = { onEvent(ProviderEvent.OnAddNew) }, // Abre un dialog para cargar los datos de proveedor
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nuevo proveedor")
            }

            // Columna de lista de proveedores. Permite scrolleo
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.providers, key = { it.provider.id ?: 0 }) { provider ->

                    ProviderRow(
                        provider = provider,
                        selected = provider.provider.id == state.selectedProvider?.provider?.id,
                        onClick = {
                            onEvent(ProviderEvent.OnProviderSelected(provider.provider))
                        }
                    )
                }
            }
        }
    }
}

// Panel de filtros, busqueda y acciones ( Disponible mediante orientación horizontal)
@Composable
fun ProviderFilterPane(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Contenedor de los elementos del panel
    Card(modifier = modifier.fillMaxHeight()) {

        // Columna de los elementos del panel
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Titulo del panel
            Text(
                text = "Filtros",
                style = MaterialTheme.typography.titleMedium
            )

            // Fila para contener el campo nombre y el botón de polaridad de orden alfabetico
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                // Campo de busqueda por nombre
                OutlinedTextField(
                    value = state.search,
                    onValueChange = { onEvent(ProviderEvent.OnSearchChange(it)) },
                    label = { Text("Nombre") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                // Botón de ordenamiento alfabetico
                IconButton(
                    onClick = { onEvent(ProviderEvent.OnSortChange(ProviderSortBy.NAME)) }
                ) {
                    SortIcon(
                        active = state.sortBy == ProviderSortBy.NAME,
                        direction = state.sortDirection
                    )
                }
            }

            // Campo de busqueda mediante cuit
            OutlinedTextField(
                value = state.cuit,
                onValueChange = { onEvent(ProviderEvent.OnCuitChange(it)) },
                label = { Text("CUIT") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Permite ingresar unicamente numeros
            )

            // Campo de busqueda mediante numero de telefono
            OutlinedTextField(
                value = state.phone,
                onValueChange = { onEvent(ProviderEvent.OnPhoneChange(it)) },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Permite ingresar numeros unicamente
            )

            // Fila de botones de aplicar y limpiar filtros
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onEvent(ProviderEvent.OnApplyFilters) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Aplicar filtros")
                }

                OutlinedButton(
                    onClick = { onEvent(ProviderEvent.OnClearFilters) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Limpiar filtros")
                }
            }
        }
    }
}

// Item de instancia de proveedor encontrado en sistema
@Composable
fun ProviderRow(
    provider: ProviderWithVisitDays,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor =
        if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.LightGray

    // Contenedor de los datos del proveedor
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 2.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        // Disposición en columna de los datos del proveedor
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(containerColor)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Nombre del proveedor
            Text(provider.provider.name, style = MaterialTheme.typography.titleMedium)
            // Cuit del proveedor
            provider.provider.cuit?.let { Text("CUIT: $it") }
            // Número de telefono del proveedor
            provider.provider.telephoneNumber?.let { Text("Telefono: $it") }
            // Dias de visita del proveedor
            Text("Visitas: ${provider.visitDays.sorted().joinToString()}")
        }
    }
}

package com.litvy.litvysales.ui.purchases.provider

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays

@Composable
fun ProviderRoute() {
    val application =
        LocalContext.current.applicationContext as LitvySalesApplication
    val container = application.container

    val factory = remember {
        ProviderViewModelFactory(
            container.getProvidersWithVisitDaysUseCase,
            container.createProviderUseCase,
            container.updateProviderUseCase
        )
    }

    val viewModel: ProviderViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProviderScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ProviderScreen(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit
) {
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProviderListPane(
                state = state,
                onEvent = onEvent,
                modifier = Modifier.weight(0.9f)
            )
            ProviderDetailPane(
                state = state,
                onEvent = onEvent,
                modifier = Modifier.weight(1.1f)
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProviderListPane(
                state = state,
                onEvent = onEvent,
                modifier = Modifier.weight(1f)
            )
            ProviderDetailPane(
                state = state,
                onEvent = onEvent,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ProviderListPane(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxHeight()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Proveedores", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = state.search,
                onValueChange = { onEvent(ProviderEvent.OnSearchChange(it)) },
                label = { Text("Buscar proveedor") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = { onEvent(ProviderEvent.OnAddNew) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nuevo proveedor")
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.filteredProviders, key = { it.provider.id ?: 0 }) { provider ->
                    ProviderRow(
                        provider = provider,
                        selected = provider.provider.id == state.selectedProvider?.provider?.id,
                        onClick = { onEvent(ProviderEvent.OnProviderSelected(provider.provider)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProviderRow(
    provider: ProviderWithVisitDays,
    selected: Boolean,
    onClick: () -> Unit
) {
    val containerColor =
        if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(containerColor)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(provider.provider.name, style = MaterialTheme.typography.titleMedium)
            provider.provider.cuit?.let { Text("CUIT: $it") }
            provider.provider.telephoneNumber?.let { Text("Telefono: $it") }
            Text("Visitas: ${provider.visitDays.sorted().joinToString()}")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProviderDetailPane(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxHeight()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = when {
                    state.isCreating -> "Nuevo proveedor"
                    state.isEditing -> "Editar proveedor"
                    state.selectedProvider != null -> "Detalle del proveedor"
                    else -> "Sin proveedor seleccionado"
                },
                style = MaterialTheme.typography.headlineSmall
            )

            if (!state.isEditing && !state.isCreating && state.selectedProvider == null) {
                Text("Selecciona un proveedor o crea uno nuevo para continuar.")
                return@Column
            }

            val readOnly = !state.isEditing

            OutlinedTextField(
                value = state.name,
                onValueChange = { onEvent(ProviderEvent.OnNameChange(it)) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !readOnly,
                isError = state.errors["name"] != null
            )
            state.errors["name"]?.let { ErrorText(it) }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = state.cuit,
                    onValueChange = { onEvent(ProviderEvent.OnCuitChange(it)) },
                    label = { Text("CUIT") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    enabled = !readOnly,
                    isError = state.errors["cuit"] != null
                )
                OutlinedTextField(
                    value = state.phone,
                    onValueChange = { onEvent(ProviderEvent.OnPhoneChange(it)) },
                    label = { Text("Telefono") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    enabled = !readOnly,
                    isError = state.errors["telephoneNumber"] != null
                )
            }
            state.errors["cuit"]?.let { ErrorText(it) }
            state.errors["telephoneNumber"]?.let { ErrorText(it) }

            OutlinedTextField(
                value = state.address,
                onValueChange = { onEvent(ProviderEvent.OnAddressChange(it)) },
                label = { Text("Direccion") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !readOnly
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = { onEvent(ProviderEvent.OnEmailChange(it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !readOnly,
                isError = state.errors["email"] != null
            )
            state.errors["email"]?.let { ErrorText(it) }

            Text("Dias de visita", style = MaterialTheme.typography.titleMedium)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dayOptions().forEach { (day, label) ->
                    val selected = day in state.visitDays
                    OutlinedButton(
                        onClick = { onEvent(ProviderEvent.OnVisitDayToggle(day)) },
                        enabled = !readOnly,
                        modifier = Modifier
                    ) {
                        Text(if (selected) "$label ✓" else label)
                    }
                }
            }

            state.feedbackMessage?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (!state.isEditing) {
                    Button(
                        onClick = { onEvent(ProviderEvent.OnEditClick) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Editar")
                    }
                } else {
                    Button(
                        onClick = { onEvent(ProviderEvent.OnSave) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (state.isCreating) "Crear proveedor" else "Guardar cambios")
                    }
                    OutlinedButton(
                        onClick = { onEvent(ProviderEvent.OnCancelEdit) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorText(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall
    )
}

private fun dayOptions(): List<Pair<Int, String>> = listOf(
    1 to "Lun",
    2 to "Mar",
    3 to "Mie",
    4 to "Jue",
    5 to "Vie",
    6 to "Sab",
    7 to "Dom"
)

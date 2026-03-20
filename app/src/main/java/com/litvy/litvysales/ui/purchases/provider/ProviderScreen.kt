package com.litvy.litvysales.ui.purchases.provider

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.domain.filter.purchases.ProviderSortBy
import com.litvy.litvysales.domain.model.purchases.ProviderWithVisitDays
import com.litvy.litvysales.ui.util.SortIcon

@Composable
fun ProviderRoute(
    navController: NavHostController
) {
    val application =
        LocalContext.current.applicationContext as LitvySalesApplication
    val container = application.container

    val factory = remember {
        ProviderViewModelFactory(
            container.getProvidersWithVisitDaysUseCase,
            container.createProviderUseCase,
            container.updateProviderUseCase,
            container.getProviderUseCase
        )
    }

    val viewModel: ProviderViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProviderScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = {
            navController.popBackStack()
        }
    )
}

@Composable
fun ProviderScreen(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit,
    onNavigateBack: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    // SNACKBAR
    LaunchedEffect(state.feedbackMessage) {
        state.feedbackMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    // NAVEGACIÓN BACK
    LaunchedEffect(state.navigateBack) {
        if (state.navigateBack) {
            onNavigateBack()
            onEvent(ProviderEvent.OnDismissDialog)
        }
    }

    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        if (isLandscape) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // IZQUIERDA — LISTA
                ProviderListPane(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.weight(0.7f)
                )

                // DERECHA — FILTROS + ACCIONES
                ProviderFilterPane(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.weight(0.3f)
                )
            }

        } else {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                ProviderListPaneVertical(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // LECTURA DE APERTURA DEL DIALOGO
        if (state.showDialog) {
            ProviderDialog(
                state = state,
                onEvent = onEvent
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onEvent(ProviderEvent.OnBack) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Proveedores",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

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

@Composable
private fun ProviderListPaneVertical(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxHeight()) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onEvent(ProviderEvent.OnBack) }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Proveedores",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            // 🔍 BUSCADOR
            OutlinedTextField(
                value = state.search,
                onValueChange = { onEvent(ProviderEvent.OnSearchChange(it)) },
                label = { Text("Buscar proveedor") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // ➕ BOTÓN NUEVO
            Button(
                onClick = { onEvent(ProviderEvent.OnAddNew) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nuevo proveedor")
            }

            // 📋 LISTA
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

@Composable
private fun ProviderFilterPane(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxHeight()) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Filtros",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = state.search,
                    onValueChange = { onEvent(ProviderEvent.OnSearchChange(it)) },
                    label = { Text("Nombre") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                IconButton(
                    onClick = { onEvent(ProviderEvent.OnSortChange(ProviderSortBy.NAME)) }
                ) {
                    SortIcon(
                        active = state.sortBy == ProviderSortBy.NAME,
                        direction = state.sortDirection
                    )
                }
            }

            OutlinedTextField(
                value = state.cuit,
                onValueChange = { onEvent(ProviderEvent.OnCuitChange(it)) },
                label = { Text("CUIT") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = state.phone,
                onValueChange = { onEvent(ProviderEvent.OnPhoneChange(it)) },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

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

            Button(
                onClick = { onEvent(ProviderEvent.OnAddNew) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nuevo proveedor")
            }
        }
    }
}

@Composable
fun ProviderDialog(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit
) {
    Dialog(
        onDismissRequest = { onEvent(ProviderEvent.OnDismissDialog) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        val isLandscape =
            LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

        Card(
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) 0.8f else 1f)
                .fillMaxHeight(0.9f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {

                    if (isLandscape) {
                        ProviderDialogContentLandscape(state, onEvent)
                    } else {
                        ProviderDialogContent(state, onEvent)
                    }
                }

                ProviderDialogActions(state, onEvent)
            }
        }
    }
}

@Composable
private fun ProviderDialogActions(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        if (!state.isEditing) {

            Button(
                onClick = { onEvent(ProviderEvent.OnEditClick) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Editar")
            }

            OutlinedButton(
                onClick = { onEvent(ProviderEvent.OnDismissDialog) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cerrar")
            }

        } else {

            Button(
                onClick = { onEvent(ProviderEvent.OnSave) },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (state.isCreating) "Crear" else "Guardar")
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProviderDialogContent(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit
) {
    val readOnly = !state.isEditing

    Text(
        text = when {
            state.isCreating -> "Nuevo proveedor"
            state.isEditing -> "Editar proveedor"
            else -> "Detalle del proveedor"
        },
        style = MaterialTheme.typography.headlineSmall
    )

    OutlinedTextField(
        value = state.name,
        onValueChange = { onEvent(ProviderEvent.OnNameChange(it)) },
        label = { Text("Nombre") },
        modifier = Modifier.fillMaxWidth(),
        enabled = !readOnly,
        isError = state.errors["name"] != null,
        singleLine = true
    )
    state.errors["name"]?.let { ErrorText(it) }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        val formattedCuit = formatCuit(state.cuit)

        val cuitField = remember(formattedCuit) {
            TextFieldValue(
                text = formattedCuit,
                selection = TextRange(formattedCuit.length)
            )
        }

        OutlinedTextField(
            value = cuitField,
            onValueChange = { value ->
                onEvent(ProviderEvent.OnCuitChange(value.text))
            },
            label = { Text("CUIT") },
            modifier = Modifier.weight(1f),
            enabled = !readOnly,
            isError = state.errors["cuit"] != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = state.phone,
            onValueChange = { onEvent(ProviderEvent.OnPhoneChange(it)) },
            label = { Text("Telefono") },
            modifier = Modifier.weight(1f),
            enabled = !readOnly,
            isError = state.errors["telephoneNumber"] != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }

    state.errors["cuit"]?.let { ErrorText(it) }
    state.errors["telephoneNumber"]?.let { ErrorText(it) }

    OutlinedTextField(
        value = state.address,
        onValueChange = { onEvent(ProviderEvent.OnAddressChange(it)) },
        label = { Text("Direccion") },
        modifier = Modifier.fillMaxWidth(),
        enabled = !readOnly,
        singleLine = true
    )

    OutlinedTextField(
        value = state.email,
        onValueChange = { onEvent(ProviderEvent.OnEmailChange(it)) },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        enabled = !readOnly,
        isError = state.errors["email"] != null,
        singleLine = true
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
                enabled = !readOnly
            ) {
                Text(if (selected) "$label ✓" else label)
            }
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProviderDialogContentLandscape(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit
) {
    val readOnly = !state.isEditing

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text(
            text = when {
                state.isCreating -> "Nuevo proveedor"
                state.isEditing -> "Editar proveedor"
                else -> "Detalle del proveedor"
            },
            style = MaterialTheme.typography.headlineSmall
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = state.name,
                    onValueChange = { onEvent(ProviderEvent.OnNameChange(it)) },
                    label = { Text("Nombre") },
                    enabled = !readOnly,
                    isError = state.errors["name"] != null,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = formatCuit(state.cuit),
                    onValueChange = { onEvent(ProviderEvent.OnCuitChange(it)) },
                    label = { Text("CUIT") },
                    enabled = !readOnly,
                    isError = state.errors["cuit"] != null,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.phone,
                    onValueChange = { onEvent(ProviderEvent.OnPhoneChange(it)) },
                    label = { Text("Telefono") },
                    enabled = !readOnly,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = state.address,
                    onValueChange = { onEvent(ProviderEvent.OnAddressChange(it)) },
                    label = { Text("Direccion") },
                    enabled = !readOnly,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = { onEvent(ProviderEvent.OnEmailChange(it)) },
                    label = { Text("Email") },
                    enabled = !readOnly,
                    isError = state.errors["email"] != null,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Dias de visita")

                FlowRow {
                    dayOptions().forEach { (day, label) ->
                        val selected = day in state.visitDays

                        OutlinedButton(
                            onClick = { onEvent(ProviderEvent.OnVisitDayToggle(day)) },
                            enabled = !readOnly
                        ) {
                            Text(if (selected) "$label ✓" else label)
                        }
                    }
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
        if (selected) MaterialTheme.colorScheme.secondaryContainer else Color.LightGray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 2.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
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

private fun formatCuit(cuit: String): String {
    val digits = cuit.filter { it.isDigit() }

    return when {
        digits.length <= 2 -> digits
        digits.length <= 10 -> "${digits.take(2)}-${digits.drop(2)}"
        else -> "${digits.take(2)}-${digits.drop(2).take(8)}-${digits.drop(10)}"
    }
}

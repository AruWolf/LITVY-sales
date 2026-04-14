package com.litvy.litvysales.ui.cashsession

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.ui.components.crud.CrudItemCard

@Composable
fun CashSessionSettingsRoute(
    navController: NavController
) {
    val application = LocalContext.current.applicationContext as LitvySalesApplication
    val container = application.container
    val factory = remember {
        CashSessionSettingsViewModelFactory(
            getCashSessionSchedulesUseCase = container.getCashSessionSchedulesUseCase,
            saveCashSessionScheduleUseCase = container.saveCashSessionScheduleUseCase,
            deleteCashSessionScheduleUseCase = container.deleteCashSessionScheduleUseCase
        )
    }
    val viewModel: CashSessionSettingsViewModel = viewModel(factory = factory)

    CashSessionSettingsScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        onBack = { navController.popBackStack() }
    )
}

@Composable
fun CashSessionSettingsScreen(
    state: CashSessionSettingsState,
    onEvent: (CashSessionSettingsEvent) -> Unit,
    onBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val isLandscape =
        LocalConfiguration.current.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(state.feedbackMessage) {
        val message = state.feedbackMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
        onEvent(CashSessionSettingsEvent.DismissFeedback)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Text(
                    text = "Configuración automática",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = { onEvent(CashSessionSettingsEvent.OpenCreateDialog) }) {
                    Text("Nueva franja")
                }
            }

            LazyColumn(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.schedules, key = { it.id }) { schedule ->
                    CrudItemCard(
                        title = buildString {
                            append(CashSessionUiFormatters.dayLabel(schedule.dayOfWeek))
                            append(" · ")
                            append(CashSessionUiFormatters.formatMinuteOfDay(schedule.openMinuteOfDay))
                            append(" - ")
                            append(CashSessionUiFormatters.formatMinuteOfDay(schedule.closeMinuteOfDay))
                            schedule.title?.takeIf { it.isNotBlank() }?.let {
                                append(" · ")
                                append(it)
                            }
                        },
                        subtitle = if (schedule.active) "Activa" else "Inactiva",
                        onEdit = { onEvent(CashSessionSettingsEvent.EditSchedule(schedule)) },
                        onDelete = { onEvent(CashSessionSettingsEvent.DeleteSchedule(schedule.id)) }
                    )
                }
            }
        }
    }

    if (state.showDialog) {
        CashSessionScheduleDialog(
            state = state,
            isLandscape = isLandscape,
            onEvent = onEvent
        )
    }
}

@Composable
private fun CashSessionScheduleDialog(
    state: CashSessionSettingsState,
    isLandscape: Boolean,
    onEvent: (CashSessionSettingsEvent) -> Unit
) {
    Dialog(
        onDismissRequest = { onEvent(CashSessionSettingsEvent.DismissDialog) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(if (isLandscape) 0.82f else 0.94f),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (state.editingId == null) "Nueva franja" else "Editar franja",
                    style = MaterialTheme.typography.titleLarge
                )

                if (isLandscape) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ScheduleTitleField(state, onEvent)
                            ScheduleDaySelector(state, onEvent)
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ScheduleTimeFields(state, onEvent)
                            ScheduleActiveSwitch(state, onEvent)
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ScheduleTitleField(state, onEvent)
                        ScheduleDaySelector(state, onEvent)
                        ScheduleTimeFields(state, onEvent)
                        ScheduleActiveSwitch(state, onEvent)
                    }
                }

                state.dialogError?.let {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    androidx.compose.material3.TextButton(
                        onClick = { onEvent(CashSessionSettingsEvent.DismissDialog) }
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = { onEvent(CashSessionSettingsEvent.SaveSchedule) }
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleTitleField(
    state: CashSessionSettingsState,
    onEvent: (CashSessionSettingsEvent) -> Unit
) {
    OutlinedTextField(
        value = state.titleInput,
        onValueChange = { onEvent(CashSessionSettingsEvent.UpdateTitle(it)) },
        label = { Text("Título (opcional)") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleDaySelector(
    state: CashSessionSettingsState,
    onEvent: (CashSessionSettingsEvent) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = CashSessionUiFormatters.dayLabel(state.selectedDayOfWeek),
            onValueChange = {},
            readOnly = true,
            label = { Text("Día") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            (1..7).forEach { day ->
                DropdownMenuItem(
                    text = { Text(CashSessionUiFormatters.dayLabel(day)) },
                    onClick = {
                        expanded = false
                        onEvent(CashSessionSettingsEvent.UpdateDayOfWeek(day))
                    }
                )
            }
        }
    }
}

@Composable
private fun ScheduleTimeFields(
    state: CashSessionSettingsState,
    onEvent: (CashSessionSettingsEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = state.openTimeInput,
            onValueChange = { onEvent(CashSessionSettingsEvent.UpdateOpenTime(it)) },
            label = { Text("Apertura") },
            modifier = Modifier.weight(1f),
            placeholder = { Text("HH:mm") },
            singleLine = true
        )
        OutlinedTextField(
            value = state.closeTimeInput,
            onValueChange = { onEvent(CashSessionSettingsEvent.UpdateCloseTime(it)) },
            label = { Text("Cierre") },
            modifier = Modifier.weight(1f),
            placeholder = { Text("HH:mm") },
            singleLine = true
        )
    }
}

@Composable
private fun ScheduleActiveSwitch(
    state: CashSessionSettingsState,
    onEvent: (CashSessionSettingsEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Activa", style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = state.activeInput,
            onCheckedChange = { onEvent(CashSessionSettingsEvent.UpdateActive(it)) }
        )
    }
}

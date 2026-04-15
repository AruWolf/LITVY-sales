package com.litvy.litvysales.ui.cashsession

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
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
import com.litvy.litvysales.domain.model.sales.CashSessionSummary
import com.litvy.litvysales.util.MoneyFormatter

@Composable
fun CashSessionListRoute(
    navController: NavController
) {
    val application = LocalContext.current.applicationContext as LitvySalesApplication
    val container = application.container
    val factory = remember {
        CashSessionListViewModelFactory(
            getCashSessionsByOpeningDateUseCase = container.getCashSessionsByOpeningDateUseCase,
            openCashSessionUseCase = container.openCashSessionUseCase
        )
    }
    val viewModel: CashSessionListViewModel = viewModel(factory = factory)

    CashSessionListScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        onBack = { navController.popBackStack() },
        onOpenSession = { sessionId ->
            navController.navigate("cashSessions/detail/$sessionId")
        }
    )
}

@Composable
fun CashSessionListScreen(
    state: CashSessionListState,
    onEvent: (CashSessionListEvent) -> Unit,
    onBack: () -> Unit,
    onOpenSession: (Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val isLandscape = LocalConfiguration.current.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(state.feedbackMessage) {
        val message = state.feedbackMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
        onEvent(CashSessionListEvent.DismissFeedback)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
                Text(
                    text = "Sesiones de caja",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )
                Button(onClick = { onEvent(CashSessionListEvent.OpenManualSessionDialog) }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text("Abrir sesión")
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onEvent(CashSessionListEvent.PreviousDay) }) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Día anterior")
                }
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null)
                    Text(CashSessionUiFormatters.formatDate(state.selectedDateMillis))
                }
                IconButton(onClick = { onEvent(CashSessionListEvent.NextDay) }) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Día siguiente")
                }
            }

            Spacer(Modifier.height(16.dp))

            if (state.sessions.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "No hay sesiones para la fecha seleccionada.",
                        modifier = Modifier.padding(24.dp)
                    )
                }
            } else if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.weight(0.7f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(state.sessions, key = { _, it -> it.sessionId }) { index, session ->
                            CashSessionSummaryCard(
                                session = session,
                                index = index,
                                onOpenSession = onOpenSession
                            )
                        }
                    }

                    Card(modifier = Modifier.weight(0.3f)) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Resumen del día", style = MaterialTheme.typography.titleMedium)
                            Text("Sesiones: ${state.sessions.size}")
                            Text(
                                "Total vendido: ${
                                    MoneyFormatter.formatFromCents(
                                        state.sessions.sumOf { it.totalSoldInCents }
                                    )
                                }"
                            )
                        }
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    itemsIndexed(
                        state.sessions,
                        key = { _, it -> it.sessionId }
                    ) { index, session ->
                        CashSessionSummaryCard(
                            session = session,
                            index = index,
                            onOpenSession = onOpenSession
                        )
                    }
                }
            }
        }
    }

    if (state.showOpenDialog) {
        AlertDialog(
            onDismissRequest = { onEvent(CashSessionListEvent.DismissManualSessionDialog) },
            title = { Text("Abrir sesión manual") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = state.openingAmountInput,
                        onValueChange = { onEvent(CashSessionListEvent.UpdateOpeningAmount(it)) },
                        label = { Text("Monto inicial (centavos)") },
                        singleLine = true,
                        isError = state.openDialogError != null
                    )
                    state.openDialogError?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            confirmButton = {
                Button(onClick = { onEvent(CashSessionListEvent.SaveManualSession) }) {
                    Text("Abrir")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { onEvent(CashSessionListEvent.DismissManualSessionDialog) }) {
                    Text("Cancelar")
                }
            }
        )
    }

    state.existingOpenSessionId?.let { sessionId ->
        AlertDialog(
            onDismissRequest = { onEvent(CashSessionListEvent.DismissExistingOpenSessionDialog) },
            title = { Text("Ya hay una sesión abierta") },
            text = {
                Text("Debés cerrar la sesión abierta antes de abrir una nueva.")
            },
            confirmButton = {
                Button(onClick = { onEvent(CashSessionListEvent.DismissExistingOpenSessionDialog) }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        onEvent(CashSessionListEvent.DismissExistingOpenSessionDialog)
                        onOpenSession(sessionId)
                    }
                ) {
                    Text("Ir a sesión abierta")
                }
            }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = state.selectedDateMillis)
        if (isLandscape) {
            Dialog(
                onDismissRequest = { showDatePicker = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .widthIn(min = 420.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(12.dp)
                        ) {
                            item {
                                DatePicker(
                                    state = datePickerState,
                                    modifier = Modifier.graphicsLayer(
                                        scaleX = 0.78f,
                                        scaleY = 0.78f
                                    )
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    datePickerState.selectedDateMillis?.let {
                                        onEvent(CashSessionListEvent.SelectDate(it))
                                    }
                                    showDatePicker = false
                                }
                            ) {
                                Text("Aceptar")
                            }
                        }
                    }
                }
            }
        } else {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                onEvent(CashSessionListEvent.SelectDate(it))
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("Aceptar")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
private fun CashSessionSummaryCard(
    session: CashSessionSummary,
    index: Int,
    onOpenSession: (Int) -> Unit
) {
    val isLandscape = LocalConfiguration.current.orientation ==
            android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onOpenSession(session.sessionId) }
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = if (isLandscape) 10.dp else 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Sesión #${index + 1}",
                style = MaterialTheme.typography.titleMedium
            )

            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Apertura: ${CashSessionUiFormatters.formatTime(session.startedAt)}")
                        Text("Cierre: ${
                            session.closedAt?.let(CashSessionUiFormatters::formatTime)
                                ?: "Abierta"
                        }")
                        Text("Usuario: ${session.openedByLabel}")
                    }

                    Text(
                        text = MoneyFormatter.formatFromCents(session.totalSoldInCents),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            } else {
                Text("Apertura: ${CashSessionUiFormatters.formatTime(session.startedAt)}")
                Text("Cierre: ${session.closedAt?.let(CashSessionUiFormatters::formatTime) ?: "Abierta"}")
                Text("Usuario: ${session.openedByLabel}")
                Text(
                    text = "Total vendido: ${MoneyFormatter.formatFromCents(session.totalSoldInCents)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
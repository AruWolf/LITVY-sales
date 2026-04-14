package com.litvy.litvysales.ui.cashsession

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.domain.model.sales.CashMovement
import com.litvy.litvysales.domain.model.sales.CashSessionDetail
import com.litvy.litvysales.domain.model.sales.CashSessionSaleSummary
import com.litvy.litvysales.util.MoneyFormatter

@Composable
fun CashSessionDetailRoute(
    navController: NavController,
    sessionId: Int
) {
    val application = LocalContext.current.applicationContext as LitvySalesApplication
    val container = application.container
    val factory = remember {
        CashSessionDetailViewModelFactory(
            sessionId = sessionId,
            getCashSessionDetailUseCase = container.getCashSessionDetailUseCase,
            registerCashMovementUseCase = container.registerCashMovementUseCase,
            closeCashSessionUseCase = container.closeCashSessionUseCase,
            updateCashSessionDifferenceJustificationUseCase = container.updateCashSessionDifferenceJustificationUseCase,
            getSaleDetailUseCase = container.getSaleDetailUseCase
        )
    }
    val viewModel: CashSessionDetailViewModel = viewModel(factory = factory)

    CashSessionDetailScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent,
        onBack = { navController.popBackStack() }
    )
}

@Composable
fun CashSessionDetailScreen(
    state: CashSessionDetailState,
    onEvent: (CashSessionDetailEvent) -> Unit,
    onBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val isLandscape =
        LocalConfiguration.current.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val detail = state.detail
    var showExpandedAmounts by remember(detail?.session?.id) { mutableStateOf(false) }

    LaunchedEffect(state.feedbackMessage) {
        val message = state.feedbackMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
        onEvent(CashSessionDetailEvent.DismissFeedback)
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
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Text(
                    text = "Detalle de sesión",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(onClick = { onEvent(CashSessionDetailEvent.OpenMovementDialog) }) {
                    Icon(Icons.Default.Payments, contentDescription = null)
                    Text("Movimiento")
                }
            }

            if (detail == null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "No se encontró la sesión.",
                        modifier = Modifier.padding(24.dp)
                    )
                }
                return@Column
            }

            Spacer(Modifier.height(8.dp))
            CashSessionHeaderCard(detail)
            Spacer(Modifier.height(8.dp))

            if (isLandscape) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CashSessionTimelineList(
                        detail = detail,
                        onEvent = onEvent,
                        modifier = Modifier.weight(0.62f)
                    )
                    Column(
                        modifier = Modifier.weight(0.38f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (shouldShowJustificationButton(detail)) {
                            Button(
                                onClick = { onEvent(CashSessionDetailEvent.OpenJustificationDialog) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Justificar diferencia")
                            }
                        }
                        if (detail.session.closedAt == null) {
                            Button(
                                onClick = { onEvent(CashSessionDetailEvent.OpenCloseDialog) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Cerrar sesión")
                            }
                        }
                        CashSessionAmountsCard(
                            detail = detail,
                            expanded = true,
                            onToggleExpanded = {}
                        )
                    }
                }
            } else {
                CashSessionTimelineList(
                    detail = detail,
                    onEvent = onEvent,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.height(8.dp))
                if (shouldShowJustificationButton(detail)) {
                    Button(
                        onClick = { onEvent(CashSessionDetailEvent.OpenJustificationDialog) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Justificar diferencia")
                    }
                    Spacer(Modifier.height(8.dp))
                }
                if (detail.session.closedAt == null) {
                    Button(
                        onClick = { onEvent(CashSessionDetailEvent.OpenCloseDialog) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cerrar sesión")
                    }
                    Spacer(Modifier.height(8.dp))
                }
                CashSessionAmountsCard(
                    detail = detail,
                    expanded = showExpandedAmounts,
                    onToggleExpanded = { showExpandedAmounts = !showExpandedAmounts }
                )
            }
        }
    }

    JustificationDialog(state, onEvent)
    MovementDialog(state, onEvent)
    CloseDialog(state, onEvent)
    SaleDetailDialog(state, onEvent)
}

@Composable
private fun CashSessionHeaderCard(detail: CashSessionDetail) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Apertura: ${CashSessionUiFormatters.formatTime(detail.session.startedAt)}")
            Text("Cierre: ${detail.session.closedAt?.let(CashSessionUiFormatters::formatTime) ?: "Abierta"}")
            Text("Usuario: Usuario #${detail.session.openedBy}")
        }
    }
}

@Composable
private fun CashSessionTimelineList(
    detail: CashSessionDetail,
    onEvent: (CashSessionDetailEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val entries = remember(detail) {
        buildList<TimelineEntry> {
            detail.sales.forEach { add(TimelineEntry.SaleEntry(it)) }
            detail.movements.forEach { add(TimelineEntry.MovementEntry(it)) }
        }.sortedByDescending { it.createdAt }
    }

    Card(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text("Ventas y movimientos", style = MaterialTheme.typography.titleLarge)
            }

            items(entries, key = { entryKey(it) }) { entry ->
                when (entry) {
                    is TimelineEntry.SaleEntry -> SaleSummaryItem(entry.sale, onEvent)
                    is TimelineEntry.MovementEntry -> MovementSummaryItem(entry.movement)
                }
            }
        }
    }
}

@Composable
private fun SaleSummaryItem(
    sale: CashSessionSaleSummary,
    onEvent: (CashSessionDetailEvent) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onEvent(CashSessionDetailEvent.OpenSaleDetail(sale.saleId)) }
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Venta ${sale.saleId}",
                style = MaterialTheme.typography.titleMedium
            )
            HorizontalDivider()
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Hora")
                Text(CashSessionUiFormatters.formatTime(sale.createdAt))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Items")
                Text("${sale.itemCount}")
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total")
                Text(
                    MoneyFormatter.formatFromCents(sale.totalInCents),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun MovementSummaryItem(movement: CashMovement) {
    val background = if (movement.type == "IN") Color(0xFFEAF7EE) else Color(0xFFFBECEC)
    val accent = if (movement.type == "IN") Color(0xFF2E7D32) else Color(0xFFC62828)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = background)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = if (movement.type == "IN") "Ingreso de efectivo" else "Extracción de efectivo",
                style = MaterialTheme.typography.titleMedium,
                color = accent
            )
            HorizontalDivider(color = accent.copy(alpha = 0.25f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Hora")
                Text(CashSessionUiFormatters.formatTime(movement.createdAt))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Motivo")
                Text(movement.reason ?: "-", textAlign = TextAlign.End)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Monto")
                Text(
                    MoneyFormatter.formatFromCents(movement.amountInCents),
                    color = accent,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CashSessionAmountsCard(
    detail: CashSessionDetail,
    expanded: Boolean,
    onToggleExpanded: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onToggleExpanded
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Monto final", style = MaterialTheme.typography.labelLarge)
                    Text(
                        detail.session.closingAmountInCents?.let(MoneyFormatter::formatFromCents) ?: "-",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    HorizontalDivider()
                    AmountRow("Monto inicial", MoneyFormatter.formatFromCents(detail.session.openingAmountInCents))
                    AmountRow("Monto esperado", MoneyFormatter.formatFromCents(detail.expectedAmountInCents))
                    AmountRow("Total vendido", MoneyFormatter.formatFromCents(detail.totalSoldInCents))
                    AmountRow(
                        "Diferencia",
                        detail.session.differenceInCents?.let(MoneyFormatter::formatFromCents) ?: "-"
                    )
                }
            }
        }
    }
}

@Composable
private fun AmountRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Text(value, fontWeight = FontWeight.Medium)
    }
}

private fun shouldShowJustificationButton(detail: CashSessionDetail): Boolean {
    return detail.session.closedAt != null && (detail.session.differenceInCents ?: 0L) != 0L
}

@Composable
private fun JustificationDialog(
    state: CashSessionDetailState,
    onEvent: (CashSessionDetailEvent) -> Unit
) {
    if (!state.showJustificationDialog) return

    AlertDialog(
        onDismissRequest = { onEvent(CashSessionDetailEvent.DismissJustificationDialog) },
        title = { Text("Justificar diferencia") },
        text = {
            OutlinedTextField(
                value = state.justificationInput,
                onValueChange = { onEvent(CashSessionDetailEvent.UpdateJustification(it)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )
        },
        confirmButton = {
            Button(onClick = { onEvent(CashSessionDetailEvent.SaveJustification) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onEvent(CashSessionDetailEvent.DismissJustificationDialog) }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun MovementDialog(
    state: CashSessionDetailState,
    onEvent: (CashSessionDetailEvent) -> Unit
) {
    if (!state.showMovementDialog) return

    AlertDialog(
        onDismissRequest = { onEvent(CashSessionDetailEvent.DismissMovementDialog) },
        title = { Text("Registrar movimiento") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = state.movementType,
                    onValueChange = { onEvent(CashSessionDetailEvent.UpdateMovementType(it.uppercase())) },
                    label = { Text("Tipo (IN/OUT)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.movementAmountInput,
                    onValueChange = { onEvent(CashSessionDetailEvent.UpdateMovementAmount(it)) },
                    label = { Text("Monto (centavos)") },
                    singleLine = true,
                    isError = state.movementDialogError != null
                )
                OutlinedTextField(
                    value = state.movementReasonInput,
                    onValueChange = { onEvent(CashSessionDetailEvent.UpdateMovementReason(it)) },
                    label = { Text("Motivo") }
                )
                state.movementDialogError?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = { onEvent(CashSessionDetailEvent.SaveMovement) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onEvent(CashSessionDetailEvent.DismissMovementDialog) }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun CloseDialog(
    state: CashSessionDetailState,
    onEvent: (CashSessionDetailEvent) -> Unit
) {
    if (!state.showCloseDialog) return

    AlertDialog(
        onDismissRequest = { onEvent(CashSessionDetailEvent.DismissCloseDialog) },
        title = { Text("Cerrar sesión") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Monto esperado: ${
                        MoneyFormatter.formatFromCents(state.detail?.expectedAmountInCents ?: 0L)
                    }"
                )
                OutlinedTextField(
                    value = state.closingAmountInput,
                    onValueChange = { onEvent(CashSessionDetailEvent.UpdateClosingAmount(it)) },
                    label = { Text("Monto final contado (centavos)") },
                    singleLine = true,
                    isError = state.closeDialogError != null
                )
                state.closeDialogError?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = { onEvent(CashSessionDetailEvent.ConfirmCloseSession) }) {
                Text("Cerrar")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onEvent(CashSessionDetailEvent.DismissCloseDialog) }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun SaleDetailDialog(
    state: CashSessionDetailState,
    onEvent: (CashSessionDetailEvent) -> Unit
) {
    val saleDetail = state.saleDetail ?: return

    AlertDialog(
        onDismissRequest = { onEvent(CashSessionDetailEvent.CloseSaleDetail) },
        title = {
            Text("Venta #${saleDetail.sale.id} - ${CashSessionUiFormatters.formatTime(saleDetail.sale.createdAt)}")
        },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(saleDetail.items, key = { it.id }) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("${saleDetail.items.indexOf(item) + 1}", fontWeight = FontWeight.Bold)
                            }
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .size(width = 1.dp, height = 42.dp)
                                    .background(MaterialTheme.colorScheme.outlineVariant)
                            )
                            Column(
                                modifier = Modifier.padding(start = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(item.productName, style = MaterialTheme.typography.titleMedium)
                                Text("Cantidad: ${item.quantity}")
                                Text("Total: ${MoneyFormatter.formatFromCents(item.totalInCents)}")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: ${MoneyFormatter.formatFromCents(saleDetail.sale.totalInCents)}",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Button(onClick = { onEvent(CashSessionDetailEvent.CloseSaleDetail) }) {
                    Text("Cerrar")
                }
            }
        }
    )
}

private sealed interface TimelineEntry {
    val createdAt: Long

    data class SaleEntry(val sale: CashSessionSaleSummary) : TimelineEntry {
        override val createdAt: Long = sale.createdAt
    }

    data class MovementEntry(val movement: CashMovement) : TimelineEntry {
        override val createdAt: Long = movement.createdAt
    }
}

private fun entryKey(entry: TimelineEntry): String = when (entry) {
    is TimelineEntry.SaleEntry -> "sale-${entry.sale.saleId}"
    is TimelineEntry.MovementEntry -> "movement-${entry.movement.id}"
}

package com.litvy.litvysales.ui.purchases.purchaseOrder.create

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.util.MoneyFormatter

// Ruteo de identificación dentro de la app
@Composable
fun PurchaseOrderCreateRoute(
    navController: NavController
) {
    val app = LocalContext.current.applicationContext as LitvySalesApplication

    val viewModel: PurchaseOrderCreateViewModel = viewModel(
        factory = PurchaseOrderCreateViewModel.Factory(app.container)
    )

    val state by viewModel.state.collectAsState()

    PurchaseOrderCreateScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = { navController.popBackStack() }
    )
}

// PANTALLA DE CREACIÓN DE ORDEN DE COMPRA
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseOrderCreateScreen(
    state: PurchaseOrderCreateState,
    onEvent: (PurchaseOrderCreateEvent) -> Unit,
    onBack: () -> Unit
) {

    var isDatePickerOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // TITULO
        Text("Nueva orden de compra", style = MaterialTheme.typography.headlineSmall)

        val selectedProviderName =
            state.providers.firstOrNull { it.id == state.selectedProviderId }?.name ?: ""

        var expanded by remember { mutableStateOf(false) }

        // CAMPO DE SELECCIÓN DE PROVEEDOR
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = selectedProviderName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Proveedor") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            // LISTA DESPLEGABLE DE PROVEEDORES
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                state.providers.forEach { provider ->
                    DropdownMenuItem(
                        text = { Text(provider.name) },
                        onClick = {
                            provider.id?.let {
                                onEvent(PurchaseOrderCreateEvent.OnProviderSelected(it))
                            }
                            expanded = false
                        }
                    )
                }
            }
        }

        // CAMPO DE SELECCION DE FECHA ESTIMADA DE LLEGADA DE LA ORDEN DE COMPRA
        Box {

            OutlinedTextField(
                value = state.expectedDate.ifBlank { "Seleccionar fecha" },
                onValueChange = {},
                readOnly = true,
                enabled = false,
                label = { Text("Fecha estimada de llegada") },
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        isDatePickerOpen = true
                    }
            )
        }

        if (isDatePickerOpen) {

            val datePickerState = androidx.compose.material3.rememberDatePickerState()

            androidx.compose.material3.DatePickerDialog(
                onDismissRequest = { isDatePickerOpen = false },
                confirmButton = {
                    Button(onClick = {
                        val millis = datePickerState.selectedDateMillis

                        millis?.let {
                            val formatted = java.text.SimpleDateFormat(
                                "dd/MM/yyyy",
                                java.util.Locale("es", "AR")
                            ).format(java.util.Date(it))

                            onEvent(
                                PurchaseOrderCreateEvent.OnDateChange(formatted)
                            )
                        }

                        isDatePickerOpen = false
                    }) {
                        Text("Aceptar")
                    }
                }
            ) {
                androidx.compose.material3.DatePicker(state = datePickerState)
            }
        }

        // SECCIÓN DE ITEMS DE PRODUCTOS SELECCIONADOS
        Text("Productos", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(state.items) { index, item ->

                // TARJETAS ITEMS DE CADA PRODUCTO CARGADO
                PurchaseOrderItemCard(
                    item = item,

                    // EVENTO DE SELECCIÓN DE PRODUCTO
                    onSelectProduct = {
                        onEvent(PurchaseOrderCreateEvent.OnOpenProductDialog(index))
                    },

                    // EVENTO DE CAMBIO DE CANTIDAD DEL PRODUCTO SELECCIONADO
                    onQuantityChange = {
                        onEvent(
                            PurchaseOrderCreateEvent.OnQuantityChange(index, it)
                        )
                    },

                    // EVENTO DE ELIMINACIÓN DE INSTANCIA DE PRODUCTO CARGADO
                    onRemove = {
                        onEvent(
                            PurchaseOrderCreateEvent.OnRemoveItem(index)
                        )
                    }
                )
            }
        }

        // VALOR ESTIMADO
        // GENERACIÓN DE VALOR TOTAL, CALCULADO EN BASE A LOS PRECIOS DE COMPRA DE CADA PRODUCTO POR SU CANTIDAD
        val total = state.items.sumOf {
            (it.suggestedUnitPriceInCents ?: 0) * it.quantity
        }.toLong()

        Text(
            text = "Total estimado: ${MoneyFormatter.formatFromCents(total)}",
            style = MaterialTheme.typography.titleMedium
        )

        // BOTON PARA AGREGAR PRODUCTO
        Button(onClick = { onEvent(PurchaseOrderCreateEvent.OnAddItem) }) {
            Text("Agregar producto")
        }

        state.error?.let {
            Text(it)
        }


        // BOTONES CANCELAR Y GUARDAR
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            Button(onClick = onBack) {
                Text("Cancelar")
            }

            Button(onClick = { onEvent(PurchaseOrderCreateEvent.OnSave) }) {
                Text("Guardar") // TODO: VERIFICAR PORQUE NO GUARDA LA ORDEN DE COMPRA GENERADA
            }
        }
    }

    // GENERACIÓN DEL DIALOG PARA LA SELECCIÓN DE UN PRODUCTO
    if (state.isProductDialogOpen) {
        Dialog(
            onDismissRequest = {
                onEvent(PurchaseOrderCreateEvent.OnCloseProductDialog)
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.85f)
                ) {

                    Column(Modifier.padding(16.dp)) {

                        val isLandscape =
                            LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

                        // ENCABEZADO
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Seleccionar producto")

                            // VISTA HORIZONTAL
                            if (isLandscape) {
                                Button(
                                    onClick = {
                                        onEvent(PurchaseOrderCreateEvent.OnCloseProductDialog)
                                    }
                                ) {
                                    Text("Cerrar")
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        // CUERPO

                        if (isLandscape) {

                            Row(Modifier.fillMaxSize()) {

                                // TODO: VERIFICAR POR QUE NO SE MUESTRA EL PANEL IZQUIERDO
                                // PANEL IZQUIERDO - FILTROS Y BUSQUEDA
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp)
                                ) {
                                    SearchAndFilters(state, onEvent)
                                }

                                // PANEL DERECHO - LISTA DE PRODUCTOS
                                ProductList(
                                    products = state.filteredProducts,
                                    state = state,
                                    onSelect = {
                                        onEvent(PurchaseOrderCreateEvent.OnProductPicked(it))
                                    },
                                    modifier = Modifier.weight(2f)
                                )
                            }

                            // VISTA VERTICAL
                        } else {

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                SearchAndFilters(state, onEvent)

                                Spacer(Modifier.height(8.dp))

                                ProductList(
                                    products = state.filteredProducts,
                                    state = state,
                                    onSelect = {
                                        onEvent(PurchaseOrderCreateEvent.OnProductPicked(it))
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    onEvent(PurchaseOrderCreateEvent.OnCloseProductDialog)
                                }
                            ) {
                                Text("Cerrar")
                            }
                        }
                    }
                }
            }

        }
    }

}


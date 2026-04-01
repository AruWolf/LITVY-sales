package com.litvy.litvysales.ui.purchases.purchaseOrder.create

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        onBack = {
            navController.popBackStack()
        },
        viewModel = viewModel,
        navController = navController
    )
}

// PANTALLA DE CREACIÓN DE ORDEN DE COMPRA
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseOrderCreateScreen(
    state: PurchaseOrderCreateState,
    onEvent: (PurchaseOrderCreateEvent) -> Unit,
    onBack: () -> Unit,
    viewModel: PurchaseOrderCreateViewModel,
    navController: NavController
) {

    var isDatePickerOpen by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("order_created", true)

                    onBack()
                }
                is UiEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // TITULO
            Text("Nueva orden de compra", style = MaterialTheme.typography.headlineSmall)

            val selectedProviderName =
                state.providers.firstOrNull { it.id == state.selectedProviderId }?.name ?: ""

            var expanded by remember { mutableStateOf(false) }

            if(isLandscape){

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ){
                    // PANEL IZQUIERDO
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end =8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // CAMPO DE SELECCIÓN DE PROVEEDOR
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {

                            OutlinedTextField(
                                value = selectedProviderName,
                                onValueChange = {},
                                isError = state.fieldErrors.containsKey("providerId"),
                                supportingText = {
                                    state.fieldErrors["providerId"]?.let {
                                        Text(it)
                                    }
                                },
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
                    }
                    // PANEL DERECHO - PRODUCTOS
                    Column(
                        modifier = Modifier
                            .weight(2f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Productos", style = MaterialTheme.typography.headlineSmall)

                            state.fieldErrors["items"]?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            // BOTON PARA AGREGAR PRODUCTO
                            Button(onClick = { onEvent(PurchaseOrderCreateEvent.OnAddItem) }) {
                                Text("Agregar producto")
                            }
                        }

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            itemsIndexed(state.items) { index, item ->

                                val productError = state.fieldErrors["items[$index].product"]
                                val quantityError = state.fieldErrors["items[$index].quantity"]

                                // TARJETAS ITEMS DE CADA PRODUCTO CARGADO
                                PurchaseOrderItemCard(
                                    item = item,
                                    errorProduct = productError,
                                    errorQuantity = quantityError,

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

                    }

                }

                // BOTONES CANCELAR, GUARDAR y valor total estimado
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Button(onClick = onBack, modifier = Modifier.weight(1f)) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = { onEvent(PurchaseOrderCreateEvent.OnSave) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar")
                    }

                    // VALOR ESTIMADO
                    // GENERACIÓN DE VALOR TOTAL, CALCULADO EN BASE A LOS PRECIOS DE COMPRA DE CADA PRODUCTO POR SU CANTIDAD
                    val total = state.items.sumOf {
                        (it.suggestedUnitPriceInCents ?: 0) * it.quantity
                    }.toLong()

                        Spacer(Modifier.weight(1f))
                        Text(
                            text = "Total estimado: ${MoneyFormatter.formatFromCents(total)}",
                            style = MaterialTheme.typography.titleMedium
                        )

                }


            }else
            {

                // CAMPO DE SELECCIÓN DE PROVEEDOR
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {

                    OutlinedTextField(
                        value = selectedProviderName,
                        onValueChange = {},
                        readOnly = true,
                        isError = state.fieldErrors.containsKey("providerId"),
                        supportingText = {
                            state.fieldErrors["providerId"]?.let{
                                Text(it)
                            }
                        },
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

                // SECCIÓN DE ITEMS DE PRODUCTOS SELECCIONADOS
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Productos", style = MaterialTheme.typography.headlineSmall)

                    // BOTON PARA AGREGAR PRODUCTO
                    Button(onClick = { onEvent(PurchaseOrderCreateEvent.OnAddItem) }) {
                        Text("Agregar producto")
                    }
                }

                state.fieldErrors["items"]?.let{
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {


                    itemsIndexed(state.items) { index, item ->

                        val productError = state.fieldErrors["items[$index].product"]
                        val quantityError = state.fieldErrors["items[$index].quantity"]

                        PurchaseOrderItemCard(
                            item = item,
                            errorProduct = productError,
                            errorQuantity = quantityError,

                            onSelectProduct = {
                                onEvent(PurchaseOrderCreateEvent.OnOpenProductDialog(index))
                            },
                            onQuantityChange = {
                                onEvent(
                                    PurchaseOrderCreateEvent.OnQuantityChange(index, it)
                                )
                            },
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

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Total estimado: ${MoneyFormatter.formatFromCents(total)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // BOTONES CANCELAR Y GUARDAR
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Button(onClick = onBack, modifier = Modifier.weight(1f)) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = { onEvent(PurchaseOrderCreateEvent.OnSave) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Guardar")
                    }
                }
            }

            if (isDatePickerOpen) {

                val datePickerState = rememberDatePickerState()

                if (!isLandscape) {

                    DatePickerDialog(
                        onDismissRequest = { isDatePickerOpen = false },
                        confirmButton = {
                            Button(onClick = {
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val formatted = java.text.SimpleDateFormat("dd/MM/yyyy")
                                        .format(java.util.Date(millis))

                                    onEvent(PurchaseOrderCreateEvent.OnDateChange(formatted))
                                }
                                isDatePickerOpen = false
                            }) {
                                Text("Aceptar")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }

                } else {

                    Dialog(
                        onDismissRequest = { isDatePickerOpen = false },
                        properties = DialogProperties(usePlatformDefaultWidth = false)
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .fillMaxHeight(0.9f)
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                DatePicker(state = datePickerState)

                                Button(
                                    onClick = {
                                        datePickerState.selectedDateMillis?.let { millis ->
                                            val formatted = java.text.SimpleDateFormat("dd/MM/yyyy")
                                                .format(java.util.Date(millis))

                                            onEvent(PurchaseOrderCreateEvent.OnDateChange(formatted))
                                        }
                                        isDatePickerOpen = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Aceptar")
                                }
                            }
                        }
                    }
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

                            if (isLandscape) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {

                                    // PANEL IZQUIERDO (FILTROS)
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .padding(end = 8.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {

                                        Text(
                                            "Filtros",
                                            style = MaterialTheme.typography.titleMedium
                                        )

                                        SearchAndFilters(state, onEvent)
                                    }

                                    // PANEL DERECHO (RESULTADOS)
                                    Column(
                                        modifier = Modifier
                                            .weight(2f)
                                            .fillMaxHeight(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {

                                        Text(
                                            "Resultados",
                                            style = MaterialTheme.typography.titleMedium
                                        )

                                        ProductList(
                                            products = state.filteredProducts,
                                            state = state,
                                            onSelect = {
                                                onEvent(PurchaseOrderCreateEvent.OnProductPicked(it))
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
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
}


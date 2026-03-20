package com.litvy.litvysales.ui.purchases.provider

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.litvy.litvysales.LitvySalesApplication
import com.litvy.litvysales.ui.purchases.provider.components.*

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

// Pantalla general del submodulo Proveedor
@Composable
fun ProviderScreen(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit,
    onNavigateBack: () -> Unit
) {

    // Alertas de eventos crear y editar
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

    // Contenedor del contenido de la pantalla
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        // Generación de componentes para orientación horizontal
        if (isLandscape) {

            // Paneles en dispuestos en la misma fila
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

        } else { // Generación de los elementos en pantalla para ventana vertical

            // Contenedor de elementos en pantalla
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Panel de proveedores
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

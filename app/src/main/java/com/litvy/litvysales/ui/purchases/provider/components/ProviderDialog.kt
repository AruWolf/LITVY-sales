package com.litvy.litvysales.ui.purchases.provider.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.litvy.litvysales.ui.purchases.provider.*

// ------------------------------------------- DIALOG -------------------------------------------

// Metodo para generar ventana emergente del tipo Dialog para la carga/edición de un proveedor
@Composable
fun ProviderDialog(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit
) {
    // Ventana emergente
    Dialog(
        onDismissRequest = { onEvent(ProviderEvent.OnDismissDialog) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        // Valor de la orientación del dispositivo
        val isLandscape =
            LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

        // Contenido del Dialog
        Card(
            modifier = Modifier
                .fillMaxWidth(if (isLandscape) 0.8f else 1f) // Cambia el ancho en base a la orientación del dispositivo
                .fillMaxHeight(0.9f)
        ) {
            // Columna para Campos y botones
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                // Columna para campos
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()) // Permite scrolleo
                ) {

                    // Si la orientación es Horizontal...
                    if (isLandscape) {
                        ProviderDialogContentLandscape(state, onEvent) // ... dibuja el contenido bajo dicha disposición
                    } else { // Si es Vertical la dibuja bajo disposición vertical
                        ProviderDialogContent(state, onEvent)
                    }
                }

                // Generación de botones de accion (Guardar/Editar y Cancelar)
                ProviderDialogActions(state, onEvent)
            }
        }
    }
}

// ------------------------------------- BOTONES DE DIALOG -------------------------------------
@Composable
fun ProviderDialogActions(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        // Si se accede al dialog mediante seleccionard un proveedor existente...
        if (!state.isEditing) {

            // ... Generar el botón con el texto "Editar" para acceder a la edición de los datos cargados
            Button(
                onClick = { onEvent(ProviderEvent.OnEditClick) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Editar")
            }

            // Botón para cerrar el dialog
            OutlinedButton(
                onClick = { onEvent(ProviderEvent.OnDismissDialog) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cerrar")
            }

            // Si se accedio mediante crear o editar...
        } else {

            // ... Generar botón para crear en caso de haber accedido desde creación
            // y guardar en caso de haber accedido desde editar
            Button(
                onClick = { onEvent(ProviderEvent.OnSave) },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (state.isCreating) "Crear" else "Guardar")
            }

            // Boton cancelar, para cerra el dialog
            OutlinedButton(
                onClick = { onEvent(ProviderEvent.OnCancelEdit) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
        }
    }
}

// ------------------------------------- CONTENIDO DE DIALOG -------------------------------------

// ------------------------------------ ORIENTACIÓN VERTICAL ------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProviderDialogContent(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit
) {
    val readOnly = !state.isEditing

    // Titulo del dialog para el caso de...
    Text(
        text = when {
            state.isCreating -> "Nuevo proveedor" // ...Crear proveedor
            state.isEditing -> "Editar proveedor" // ...Editar proveedor
            else -> "Detalle del proveedor" // ...Ver datos del proveedor
        },
        style = MaterialTheme.typography.headlineSmall
    )

    // Campo para cargar/ver el nombre del proveedor
    OutlinedTextField(
        value = state.name,
        onValueChange = { onEvent(ProviderEvent.OnNameChange(it)) },
        label = { Text("Nombre") },
        modifier = Modifier.fillMaxWidth(),
        enabled = !readOnly, // En caso de estar en modo vista, bloquear la carga de datos al campo
        isError = state.errors["name"] != null,
        singleLine = true
    )
    state.errors["name"]?.let { ErrorText(it) } // Dibujar mensaje de error correspondiente


    // Campos cuit y telephoneNumber generadas en una misma fila
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        val formattedCuit = formatCuit(state.cuit)

        // Formateo del cuit, para generar "-" en la separación lógica del numero
        val cuitField = remember(formattedCuit) {
            TextFieldValue(
                text = formattedCuit,
                selection = TextRange(formattedCuit.length)
            )
        }

        // Campo de cuit
        OutlinedTextField(
            value = cuitField,
            onValueChange = { value ->
                onEvent(ProviderEvent.OnCuitChange(value.text))
            },
            label = { Text("CUIT") },
            modifier = Modifier.weight(1f),
            enabled = !readOnly, // Bloqueo de campo en caso de estar en modo vista
            isError = state.errors["cuit"] != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Se puede ingresar unicamente numeros mediante teclado
        )

        // Campo de numero de telefono
        OutlinedTextField(
            value = state.phone,
            onValueChange = { onEvent(ProviderEvent.OnPhoneChange(it)) },
            label = { Text("Telefono") },
            modifier = Modifier.weight(1f),
            enabled = !readOnly, // Bloqueo de campo en caso de estar en modo vista
            isError = state.errors["telephoneNumber"] != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Se puede ingresar unicamente numeros mediante teclado
        )
    }

    // Generador de errores para cuit y numero de telefono
    state.errors["cuit"]?.let { ErrorText(it) }
    state.errors["telephoneNumber"]?.let { ErrorText(it) }

    // Campo Dirección
    OutlinedTextField(
        value = state.address,
        onValueChange = { onEvent(ProviderEvent.OnAddressChange(it)) },
        label = { Text("Direccion") },
        modifier = Modifier.fillMaxWidth(),
        enabled = !readOnly, // Bloqueo de campo en caso de estar en modo vista
        singleLine = true
    )

    OutlinedTextField(
        value = state.email,
        onValueChange = { onEvent(ProviderEvent.OnEmailChange(it)) },
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        enabled = !readOnly, // Bloqueo de campo en caso de estar en modo vista
        isError = state.errors["email"] != null,
        singleLine = true
    )
    state.errors["email"]?.let { ErrorText(it) } // Generador de errores para el campo de email

    // Sección de dias de visita
    Text("Dias de visita", style = MaterialTheme.typography.titleMedium)

    // Fila que genera los dias seleccionables de visita del proveedor por crear/editar
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        dayOptions().forEach { (day, label) -> // Recorre el metodo para generar los botones de los dias de visita
            val selected = day in state.visitDays

            // Botón genérico para cada dia disponible
            OutlinedButton(
                onClick = { onEvent(ProviderEvent.OnVisitDayToggle(day)) },
                enabled = !readOnly // Bloqueo de botón en caso de estar en modo vista
            ) {
                Text(if (selected) "$label ✓" else label)
            }
        }
    }

}

// ------------------------------------ ORIENTACIÓN HORIZONTAL ------------------------------------

// Mismo contenido que orientación vertical, diferente disposición de elementos
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProviderDialogContentLandscape(
    state: ProviderState,
    onEvent: (ProviderEvent) -> Unit
) {
    val readOnly = !state.isEditing

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // Titulo del dialog para el caso de...
        Text(
            text = when {
                state.isCreating -> "Nuevo proveedor" // ...Crear proveedor
                state.isEditing -> "Editar proveedor" // ...Editar proveedor
                else -> "Detalle del proveedor" // ...Ver datos del proveedor
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


// ------------------------------------------- UTIL -------------------------------------------

// Metodo para manejar errores de campos de carga/edición
@Composable
fun ErrorText(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall
    )
}

// Metodo que contiene los dias de la semana enumerados
fun dayOptions(): List<Pair<Int, String>> = listOf(
    1 to "Lun",
    2 to "Mar",
    3 to "Mie",
    4 to "Jue",
    5 to "Vie",
    6 to "Sab",
    7 to "Dom"
)

// Metodo para formatear el valor de cuit bajo el esquema "xx-xxxxxxxx-x"
fun formatCuit(cuit: String): String {
    val digits = cuit.filter { it.isDigit() }

    return when {
        digits.length <= 2 -> digits
        digits.length <= 10 -> "${digits.take(2)}-${digits.drop(2)}"
        else -> "${digits.take(2)}-${digits.drop(2).take(8)}-${digits.drop(10)}"
    }
}

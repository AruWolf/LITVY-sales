package com.litvy.litvysales.ui.systemparameters.invoicetype

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.domain.model.purchases.InvoiceType
import kotlinx.coroutines.launch


class InvoiceTypesViewModel(
    // Casos de uso
): ViewModel() {

    var state by mutableStateOf(InvoiceTypeState())
        private set

    private fun loadInvoiceTypes() {
        viewModelScope.launch {
            val items = emptyList<InvoiceType>() //TODO: Aplicar getter
            state = state.copy(items = items)
        }
    }

    init{
        loadInvoiceTypes()
    }

    fun onEvent(event: InvoiceTypeEvent){
        when(event){

            InvoiceTypeEvent.OnCreateClick -> {
                state = state.copy(
                    showDialog = true,
                    editingItem = null,
                    code = "",
                    description = ""
                )
            }

            InvoiceTypeEvent.OnDismissDialog -> {
                state = state.copy(showDialog = false)
            }

            is InvoiceTypeEvent.OnCodeChange -> {
                state = state.copy(code = event.value)
            }

            is InvoiceTypeEvent.OnDescriptionChange -> {
                state = state.copy(description = event.value)
            }
            /*
            InvoiceTypeEvent.OnSave -> {
                val isEditing = state.editingItem != null

                val invoiceType: InvoiceType(
                    id = state.editingItem?.id ?: 0,
                    code = state.code,
                    description = state.description
                )
            }*/

            else -> {}
        }
    }
}


class InvoiceTypeViewModelFactory(){

}
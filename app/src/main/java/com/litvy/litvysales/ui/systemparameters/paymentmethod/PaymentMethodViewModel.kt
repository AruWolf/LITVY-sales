package com.litvy.litvysales.ui.systemparameters.paymentmethod

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.domain.model.util.PaymentMethod
import com.litvy.litvysales.domain.useCase.sales.paymentmethod.CreatePaymentMethodUseCase
import com.litvy.litvysales.domain.useCase.sales.paymentmethod.DeletePaymentMethodUseCase
import com.litvy.litvysales.domain.useCase.sales.paymentmethod.GetPaymentMethodsUseCase
import com.litvy.litvysales.domain.useCase.sales.paymentmethod.UpdatePaymentMethodUseCase
import com.litvy.litvysales.domain.validation.ValidationResult
import kotlinx.coroutines.launch


// Clase orquestadora de eventos del submodulo de metodos de pago
class PaymentMethodViewModel(
    private val createPaymentMethod: CreatePaymentMethodUseCase,
    private val getPaymentMethod: GetPaymentMethodsUseCase,
    private val updatePaymentMethod: UpdatePaymentMethodUseCase,
    private val deletePaymentMethod: DeletePaymentMethodUseCase
) : ViewModel() {

    var state by mutableStateOf(PaymentMethodState())
        private set

    private fun loadPaymentMethods() {
        viewModelScope.launch {
            val items = getPaymentMethod()
            state = state.copy(items = items)
        }
    }

    init {
        loadPaymentMethods()
    }

    // Asignación de acciones para cada evento
    fun onEvent(event: PaymentMethodEvent) {
        when (event) {

            // EVENTO DE CREACIÓN
            PaymentMethodEvent.OnCreateClick -> {
                state = state.copy(
                    showDialog = true,
                    editingItem = null,
                    name = "",
                    surcharge = ""
                )
            }

            // EVENTO DE CIERRE DE DIALOG
            PaymentMethodEvent.OnDismissDialog -> {
                state = state.copy(showDialog = false)
            }

            // EVENTO DE CAMBIO DE NOMBRE
            is PaymentMethodEvent.OnNameChange -> {
                state = state.copy(name = event.value)
            }

            is PaymentMethodEvent.OnSurchargeChange -> {
                state = state.copy(surcharge = event.value)
            }

            // EVENTO DE GUARDADO
            PaymentMethodEvent.OnSave -> {

                val surchargeValue = state.surcharge.toDoubleOrNull() ?: 0.0

                val isEditing = state.editingItem != null

                val paymentMethod = PaymentMethod(
                    id = state.editingItem?.id ?: 0,
                    name = state.name,
                    surchargePercentage = surchargeValue
                )

                viewModelScope.launch {

                    val result = if (isEditing) {
                        updatePaymentMethod(paymentMethod)
                    } else {
                        createPaymentMethod(paymentMethod)
                    }

                    when (result) {
                        is ValidationResult.Success -> {
                            loadPaymentMethods()
                            state = state.copy(
                                showDialog = false,
                                errors = emptyMap(),
                                editingItem = null
                            )
                        }

                        is ValidationResult.Failure -> {
                            val errorMap = result.errors.associate { it.field to it.message }
                            state = state.copy(errors = errorMap)
                        }
                    }
                }
            }



            // EVENTO DE ACTUALIZACIÓN
            is PaymentMethodEvent.OnEdit -> {
                state = state.copy(
                    showDialog = true,
                    editingItem = event.item,
                    name = event.item.name,
                    surcharge = event.item.surchargePercentage.toString()
                )
            }

            // EVENTO DE ELMINACIÓN
            is PaymentMethodEvent.OnDelete -> {
                viewModelScope.launch {
                    deletePaymentMethod(event.item)
                    loadPaymentMethods()
                }
            }

            else -> {}
        }
    }
}


class PaymentMethodViewModelFactory(
    private val createPaymentMethod: CreatePaymentMethodUseCase,
    private val getPaymentMethod: GetPaymentMethodsUseCase,
    private val updatePaymentMethod: UpdatePaymentMethodUseCase,
    private val deletePaymentMethod: DeletePaymentMethodUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PaymentMethodViewModel(
            createPaymentMethod,
            getPaymentMethod,
            updatePaymentMethod,
            deletePaymentMethod
        ) as T
    }
}
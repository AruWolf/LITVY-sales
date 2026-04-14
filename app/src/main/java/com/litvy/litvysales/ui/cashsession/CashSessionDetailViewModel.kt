package com.litvy.litvysales.ui.cashsession

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.domain.model.sales.CashSessionDefaults
import com.litvy.litvysales.domain.model.sales.CashSessionDetail
import com.litvy.litvysales.domain.model.sales.SaleDetail
import com.litvy.litvysales.domain.useCase.sales.GetSaleDetailUseCase
import com.litvy.litvysales.domain.useCase.sales.cashSession.CloseCashSessionUseCase
import com.litvy.litvysales.domain.useCase.sales.cashSession.GetCashSessionDetailUseCase
import com.litvy.litvysales.domain.useCase.sales.cashSession.RegisterCashMovementUseCase
import com.litvy.litvysales.domain.useCase.sales.cashSession.UpdateCashSessionDifferenceJustificationUseCase
import kotlinx.coroutines.launch

data class CashSessionDetailState(
    val sessionId: Int,
    val detail: CashSessionDetail? = null,
    val saleDetail: SaleDetail? = null,
    val isLoading: Boolean = false,
    val feedbackMessage: String? = null,
    val justificationInput: String = "",
    val showJustificationDialog: Boolean = false,
    val showMovementDialog: Boolean = false,
    val movementType: String = "IN",
    val movementAmountInput: String = "",
    val movementReasonInput: String = "",
    val movementDialogError: String? = null,
    val showCloseDialog: Boolean = false,
    val closingAmountInput: String = "",
    val closeDialogError: String? = null
)

sealed interface CashSessionDetailEvent {
    data object Refresh : CashSessionDetailEvent
    data class UpdateJustification(val value: String) : CashSessionDetailEvent
    data object OpenJustificationDialog : CashSessionDetailEvent
    data object DismissJustificationDialog : CashSessionDetailEvent
    data object SaveJustification : CashSessionDetailEvent
    data object OpenMovementDialog : CashSessionDetailEvent
    data object DismissMovementDialog : CashSessionDetailEvent
    data class UpdateMovementType(val value: String) : CashSessionDetailEvent
    data class UpdateMovementAmount(val value: String) : CashSessionDetailEvent
    data class UpdateMovementReason(val value: String) : CashSessionDetailEvent
    data object SaveMovement : CashSessionDetailEvent
    data object OpenCloseDialog : CashSessionDetailEvent
    data object DismissCloseDialog : CashSessionDetailEvent
    data class UpdateClosingAmount(val value: String) : CashSessionDetailEvent
    data object ConfirmCloseSession : CashSessionDetailEvent
    data class OpenSaleDetail(val saleId: Int) : CashSessionDetailEvent
    data object CloseSaleDetail : CashSessionDetailEvent
    data object DismissFeedback : CashSessionDetailEvent
}

class CashSessionDetailViewModel(
    private val sessionId: Int,
    private val getCashSessionDetailUseCase: GetCashSessionDetailUseCase,
    private val registerCashMovementUseCase: RegisterCashMovementUseCase,
    private val closeCashSessionUseCase: CloseCashSessionUseCase,
    private val updateCashSessionDifferenceJustificationUseCase: UpdateCashSessionDifferenceJustificationUseCase,
    private val getSaleDetailUseCase: GetSaleDetailUseCase
) : ViewModel() {

    var state by mutableStateOf(CashSessionDetailState(sessionId = sessionId))
        private set

    init {
        refresh()
    }

    fun onEvent(event: CashSessionDetailEvent) {
        when (event) {
            CashSessionDetailEvent.Refresh -> refresh()
            is CashSessionDetailEvent.UpdateJustification -> {
                state = state.copy(justificationInput = event.value)
            }
            CashSessionDetailEvent.OpenJustificationDialog -> {
                state = state.copy(showJustificationDialog = true)
            }
            CashSessionDetailEvent.DismissJustificationDialog -> {
                state = state.copy(showJustificationDialog = false)
            }
            CashSessionDetailEvent.SaveJustification -> saveJustification()
            CashSessionDetailEvent.OpenMovementDialog -> {
                state = state.copy(showMovementDialog = true, movementDialogError = null)
            }
            CashSessionDetailEvent.DismissMovementDialog -> {
                state = state.copy(
                    showMovementDialog = false,
                    movementType = "IN",
                    movementAmountInput = "",
                    movementReasonInput = "",
                    movementDialogError = null
                )
            }
            is CashSessionDetailEvent.UpdateMovementType -> {
                state = state.copy(movementType = event.value)
            }
            is CashSessionDetailEvent.UpdateMovementAmount -> {
                state = state.copy(movementAmountInput = event.value, movementDialogError = null)
            }
            is CashSessionDetailEvent.UpdateMovementReason -> {
                state = state.copy(movementReasonInput = event.value, movementDialogError = null)
            }
            CashSessionDetailEvent.SaveMovement -> saveMovement()
            CashSessionDetailEvent.OpenCloseDialog -> {
                state = state.copy(showCloseDialog = true, closeDialogError = null)
            }
            CashSessionDetailEvent.DismissCloseDialog -> {
                state = state.copy(
                    showCloseDialog = false,
                    closingAmountInput = "",
                    closeDialogError = null
                )
            }
            is CashSessionDetailEvent.UpdateClosingAmount -> {
                state = state.copy(closingAmountInput = event.value, closeDialogError = null)
            }
            CashSessionDetailEvent.ConfirmCloseSession -> closeSession()
            is CashSessionDetailEvent.OpenSaleDetail -> openSaleDetail(event.saleId)
            CashSessionDetailEvent.CloseSaleDetail -> {
                state = state.copy(saleDetail = null)
            }
            CashSessionDetailEvent.DismissFeedback -> {
                state = state.copy(feedbackMessage = null)
            }
        }
    }

    private fun refresh() {
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            runCatching { getCashSessionDetailUseCase(sessionId) }
                .onSuccess { detail ->
                    state = state.copy(
                        detail = detail,
                        isLoading = false,
                        justificationInput = detail?.session?.differenceJustification.orEmpty()
                    )
                }
                .onFailure { error ->
                    state = state.copy(
                        isLoading = false,
                        feedbackMessage = error.message ?: "No se pudo cargar la sesión."
                    )
                }
        }
    }

    private fun saveMovement() {
        val amount = state.movementAmountInput.toLongOrNull()
        if (amount == null || amount <= 0L) {
            state = state.copy(movementDialogError = "Ingresá un monto válido.")
            return
        }

        viewModelScope.launch {
            runCatching {
                registerCashMovementUseCase(
                    sessionId = sessionId,
                    type = state.movementType,
                    amountInCents = amount,
                    reason = state.movementReasonInput,
                    createdBy = CashSessionDefaults.DEFAULT_USER_ID
                )
            }.onSuccess {
                state = state.copy(
                    showMovementDialog = false,
                    movementType = "IN",
                    movementAmountInput = "",
                    movementReasonInput = "",
                    movementDialogError = null,
                    feedbackMessage = "Movimiento registrado."
                )
                refresh()
            }.onFailure { error ->
                state = state.copy(
                    movementDialogError = error.message ?: "No se pudo registrar el movimiento."
                )
            }
        }
    }

    private fun closeSession() {
        val amount = state.closingAmountInput.toLongOrNull()
        if (amount == null || amount < 0L) {
            state = state.copy(closeDialogError = "Ingresá un monto válido.")
            return
        }

        viewModelScope.launch {
            runCatching {
                closeCashSessionUseCase(
                    sessionId = sessionId,
                    closingAmountInCents = amount,
                    closedBy = CashSessionDefaults.DEFAULT_USER_ID,
                    justification = state.justificationInput
                )
            }.onSuccess {
                state = state.copy(
                    showCloseDialog = false,
                    closingAmountInput = "",
                    closeDialogError = null,
                    feedbackMessage = "Sesión cerrada correctamente.",
                    showJustificationDialog = false
                )
                refresh()
            }.onFailure { error ->
                state = state.copy(closeDialogError = error.message ?: "No se pudo cerrar la sesión.")
            }
        }
    }

    private fun saveJustification() {
        viewModelScope.launch {
            runCatching {
                updateCashSessionDifferenceJustificationUseCase(sessionId, state.justificationInput)
            }.onSuccess {
                state = state.copy(
                    feedbackMessage = "Justificación guardada.",
                    showJustificationDialog = false
                )
                refresh()
            }.onFailure { error ->
                state = state.copy(feedbackMessage = error.message ?: "No se pudo guardar la justificación.")
            }
        }
    }

    private fun openSaleDetail(saleId: Int) {
        viewModelScope.launch {
            runCatching { getSaleDetailUseCase(saleId) }
                .onSuccess { saleDetail ->
                    state = state.copy(saleDetail = saleDetail)
                }
                .onFailure { error ->
                    state = state.copy(
                        feedbackMessage = error.message ?: "No se pudo abrir el detalle de la venta."
                    )
                }
        }
    }
}

class CashSessionDetailViewModelFactory(
    private val sessionId: Int,
    private val getCashSessionDetailUseCase: GetCashSessionDetailUseCase,
    private val registerCashMovementUseCase: RegisterCashMovementUseCase,
    private val closeCashSessionUseCase: CloseCashSessionUseCase,
    private val updateCashSessionDifferenceJustificationUseCase: UpdateCashSessionDifferenceJustificationUseCase,
    private val getSaleDetailUseCase: GetSaleDetailUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CashSessionDetailViewModel(
            sessionId = sessionId,
            getCashSessionDetailUseCase = getCashSessionDetailUseCase,
            registerCashMovementUseCase = registerCashMovementUseCase,
            closeCashSessionUseCase = closeCashSessionUseCase,
            updateCashSessionDifferenceJustificationUseCase = updateCashSessionDifferenceJustificationUseCase,
            getSaleDetailUseCase = getSaleDetailUseCase
        ) as T
    }
}

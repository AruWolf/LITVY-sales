package com.litvy.litvysales.ui.cashsession

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.domain.model.sales.CashSessionDefaults
import com.litvy.litvysales.domain.model.sales.CashSessionSummary
import com.litvy.litvysales.domain.useCase.sales.cashSession.CashSessionAlreadyOpenException
import com.litvy.litvysales.domain.useCase.sales.cashSession.GetCashSessionsByOpeningDateUseCase
import com.litvy.litvysales.domain.useCase.sales.cashSession.OpenCashSessionUseCase
import kotlinx.coroutines.launch

data class CashSessionListState(
    val selectedDateMillis: Long = CashSessionUiFormatters.startOfDay(System.currentTimeMillis()),
    val sessions: List<CashSessionSummary> = emptyList(),
    val isLoading: Boolean = false,
    val feedbackMessage: String? = null,
    val showOpenDialog: Boolean = false,
    val openingAmountInput: String = "",
    val openDialogError: String? = null,
    val existingOpenSessionId: Int? = null
)

sealed interface CashSessionListEvent {
    data object PreviousDay : CashSessionListEvent
    data object NextDay : CashSessionListEvent
    data class SelectDate(val dateMillis: Long) : CashSessionListEvent
    data object OpenManualSessionDialog : CashSessionListEvent
    data object DismissManualSessionDialog : CashSessionListEvent
    data class UpdateOpeningAmount(val value: String) : CashSessionListEvent
    data object SaveManualSession : CashSessionListEvent
    data object DismissExistingOpenSessionDialog : CashSessionListEvent
    data object DismissFeedback : CashSessionListEvent
}

class CashSessionListViewModel(
    private val getCashSessionsByOpeningDateUseCase: GetCashSessionsByOpeningDateUseCase,
    private val openCashSessionUseCase: OpenCashSessionUseCase
) : ViewModel() {

    var state by mutableStateOf(CashSessionListState())
        private set

    init {
        loadSessions()
    }

    fun onEvent(event: CashSessionListEvent) {
        when (event) {
            CashSessionListEvent.PreviousDay -> {
                state = state.copy(
                    selectedDateMillis = CashSessionUiFormatters.changeDay(state.selectedDateMillis, -1)
                )
                loadSessions()
            }
            CashSessionListEvent.NextDay -> {
                state = state.copy(
                    selectedDateMillis = CashSessionUiFormatters.changeDay(state.selectedDateMillis, 1)
                )
                loadSessions()
            }
            is CashSessionListEvent.SelectDate -> {
                state = state.copy(
                    selectedDateMillis = CashSessionUiFormatters.startOfDay(event.dateMillis)
                )
                loadSessions()
            }
            CashSessionListEvent.OpenManualSessionDialog -> {
                state = state.copy(showOpenDialog = true, openDialogError = null)
            }
            CashSessionListEvent.DismissManualSessionDialog -> {
                state = state.copy(
                    showOpenDialog = false,
                    openingAmountInput = "",
                    openDialogError = null
                )
            }
            is CashSessionListEvent.UpdateOpeningAmount -> {
                state = state.copy(
                    openingAmountInput = event.value,
                    openDialogError = null
                )
            }
            CashSessionListEvent.SaveManualSession -> openManualSession()
            CashSessionListEvent.DismissExistingOpenSessionDialog -> {
                state = state.copy(existingOpenSessionId = null)
            }
            CashSessionListEvent.DismissFeedback -> {
                state = state.copy(feedbackMessage = null)
            }
        }
    }

    private fun loadSessions() {
        val selectedDate = state.selectedDateMillis
        state = state.copy(isLoading = true)
        viewModelScope.launch {
            runCatching {
                getCashSessionsByOpeningDateUseCase(
                    startOfDayMillis = CashSessionUiFormatters.startOfDay(selectedDate),
                    endOfDayMillis = CashSessionUiFormatters.endOfDay(selectedDate)
                )
            }.onSuccess { sessions ->
                state = state.copy(
                    sessions = sessions,
                    isLoading = false
                )
            }.onFailure { error ->
                state = state.copy(
                    isLoading = false,
                    feedbackMessage = error.message ?: "No se pudieron cargar las sesiones."
                )
            }
        }
    }

    private fun openManualSession() {
        val amount = state.openingAmountInput.toLongOrNull()
        if (amount == null || amount < 0L) {
            state = state.copy(openDialogError = "Ingresá un monto válido en centavos.")
            return
        }

        viewModelScope.launch {
            runCatching {
                openCashSessionUseCase(
                    registerId = CashSessionDefaults.DEFAULT_CASH_REGISTER_ID,
                    openedBy = CashSessionDefaults.DEFAULT_USER_ID,
                    openingAmountInCents = amount
                )
            }.onSuccess {
                state = state.copy(
                    showOpenDialog = false,
                    openingAmountInput = "",
                    openDialogError = null,
                    feedbackMessage = "Sesión abierta correctamente."
                )
                loadSessions()
            }.onFailure { error ->
                if (error is CashSessionAlreadyOpenException) {
                    state = state.copy(
                        showOpenDialog = false,
                        existingOpenSessionId = error.sessionId
                    )
                } else {
                    state = state.copy(
                        openDialogError = error.message ?: "No se pudo abrir la sesión."
                    )
                }
            }
        }
    }
}

class CashSessionListViewModelFactory(
    private val getCashSessionsByOpeningDateUseCase: GetCashSessionsByOpeningDateUseCase,
    private val openCashSessionUseCase: OpenCashSessionUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CashSessionListViewModel(
            getCashSessionsByOpeningDateUseCase = getCashSessionsByOpeningDateUseCase,
            openCashSessionUseCase = openCashSessionUseCase
        ) as T
    }
}

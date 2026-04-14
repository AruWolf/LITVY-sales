package com.litvy.litvysales.ui.cashsession

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.litvy.litvysales.domain.model.sales.CashSessionSchedule
import com.litvy.litvysales.domain.useCase.sales.cashSession.DeleteCashSessionScheduleUseCase
import com.litvy.litvysales.domain.useCase.sales.cashSession.GetCashSessionSchedulesUseCase
import com.litvy.litvysales.domain.useCase.sales.cashSession.SaveCashSessionScheduleUseCase
import kotlinx.coroutines.launch

data class CashSessionSettingsState(
    val schedules: List<CashSessionSchedule> = emptyList(),
    val feedbackMessage: String? = null,
    val showDialog: Boolean = false,
    val editingId: Int? = null,
    val titleInput: String = "",
    val selectedDayOfWeek: Int = 1,
    val openTimeInput: String = "09:00",
    val closeTimeInput: String = "18:00",
    val activeInput: Boolean = true,
    val dialogError: String? = null
)

sealed interface CashSessionSettingsEvent {
    data object OpenCreateDialog : CashSessionSettingsEvent
    data object DismissDialog : CashSessionSettingsEvent
    data class EditSchedule(val schedule: CashSessionSchedule) : CashSessionSettingsEvent
    data class DeleteSchedule(val scheduleId: Int) : CashSessionSettingsEvent
    data class UpdateTitle(val value: String) : CashSessionSettingsEvent
    data class UpdateDayOfWeek(val value: Int) : CashSessionSettingsEvent
    data class UpdateOpenTime(val value: String) : CashSessionSettingsEvent
    data class UpdateCloseTime(val value: String) : CashSessionSettingsEvent
    data class UpdateActive(val value: Boolean) : CashSessionSettingsEvent
    data object SaveSchedule : CashSessionSettingsEvent
    data object DismissFeedback : CashSessionSettingsEvent
}

class CashSessionSettingsViewModel(
    private val getCashSessionSchedulesUseCase: GetCashSessionSchedulesUseCase,
    private val saveCashSessionScheduleUseCase: SaveCashSessionScheduleUseCase,
    private val deleteCashSessionScheduleUseCase: DeleteCashSessionScheduleUseCase
) : ViewModel() {

    var state by mutableStateOf(CashSessionSettingsState())
        private set

    init {
        viewModelScope.launch {
            getCashSessionSchedulesUseCase().collect { schedules ->
                state = state.copy(schedules = schedules)
            }
        }
    }

    fun onEvent(event: CashSessionSettingsEvent) {
        when (event) {
            CashSessionSettingsEvent.OpenCreateDialog -> {
                state = state.copy(
                    showDialog = true,
                    editingId = null,
                    titleInput = "",
                    selectedDayOfWeek = 1,
                    openTimeInput = "09:00",
                    closeTimeInput = "18:00",
                    activeInput = true,
                    dialogError = null
                )
            }
            CashSessionSettingsEvent.DismissDialog -> {
                state = state.copy(showDialog = false, editingId = null, dialogError = null)
            }
            is CashSessionSettingsEvent.EditSchedule -> {
                state = state.copy(
                    showDialog = true,
                    editingId = event.schedule.id,
                    titleInput = event.schedule.title.orEmpty(),
                    selectedDayOfWeek = event.schedule.dayOfWeek,
                    openTimeInput = CashSessionUiFormatters.formatMinuteOfDay(event.schedule.openMinuteOfDay),
                    closeTimeInput = CashSessionUiFormatters.formatMinuteOfDay(event.schedule.closeMinuteOfDay),
                    activeInput = event.schedule.active,
                    dialogError = null
                )
            }
            is CashSessionSettingsEvent.DeleteSchedule -> deleteSchedule(event.scheduleId)
            is CashSessionSettingsEvent.UpdateTitle -> state = state.copy(titleInput = event.value, dialogError = null)
            is CashSessionSettingsEvent.UpdateDayOfWeek -> state = state.copy(selectedDayOfWeek = event.value, dialogError = null)
            is CashSessionSettingsEvent.UpdateOpenTime -> state = state.copy(openTimeInput = formatTimeInput(event.value), dialogError = null)
            is CashSessionSettingsEvent.UpdateCloseTime -> state = state.copy(closeTimeInput = formatTimeInput(event.value), dialogError = null)
            is CashSessionSettingsEvent.UpdateActive -> state = state.copy(activeInput = event.value, dialogError = null)
            CashSessionSettingsEvent.SaveSchedule -> saveSchedule()
            CashSessionSettingsEvent.DismissFeedback -> state = state.copy(feedbackMessage = null)
        }
    }

    private fun saveSchedule() {
        val openMinute = CashSessionUiFormatters.parseMinuteOfDay(state.openTimeInput)
        val closeMinute = CashSessionUiFormatters.parseMinuteOfDay(state.closeTimeInput)

        if (openMinute == null || closeMinute == null) {
            state = state.copy(dialogError = "Ingresá horarios válidos con formato HH:mm.")
            return
        }

        if (state.activeInput && hasOverlap(openMinute, closeMinute)) {
            state = state.copy(dialogError = "La franja activa se superpone con otra sesión automática ya creada.")
            return
        }

        viewModelScope.launch {
            runCatching {
                saveCashSessionScheduleUseCase(
                    CashSessionSchedule(
                        id = state.editingId ?: 0,
                        title = state.titleInput,
                        dayOfWeek = state.selectedDayOfWeek,
                        openMinuteOfDay = openMinute,
                        closeMinuteOfDay = closeMinute,
                        active = state.activeInput
                    )
                )
            }.onSuccess {
                state = state.copy(
                    showDialog = false,
                    editingId = null,
                    dialogError = null,
                    feedbackMessage = "Franja guardada correctamente."
                )
            }.onFailure { error ->
                state = state.copy(dialogError = error.message ?: "No se pudo guardar la franja.")
            }
        }
    }

    private fun hasOverlap(openMinute: Int, closeMinute: Int): Boolean {
        return state.schedules
            .filter { schedule ->
                schedule.active &&
                    schedule.dayOfWeek == state.selectedDayOfWeek &&
                    schedule.id != state.editingId
            }
            .any { schedule ->
                openMinute < schedule.closeMinuteOfDay && closeMinute > schedule.openMinuteOfDay
            }
    }

    private fun deleteSchedule(scheduleId: Int) {
        viewModelScope.launch {
            runCatching { deleteCashSessionScheduleUseCase(scheduleId) }
                .onSuccess {
                    state = state.copy(feedbackMessage = "Franja eliminada.")
                }
                .onFailure { error ->
                    state = state.copy(feedbackMessage = error.message ?: "No se pudo eliminar la franja.")
                }
        }
    }

    private fun formatTimeInput(raw: String): String {
        val digits = raw.filter { it.isDigit() }.take(4)
        return when {
            digits.length <= 2 -> digits
            else -> "${digits.take(2)}:${digits.drop(2)}"
        }
    }
}

class CashSessionSettingsViewModelFactory(
    private val getCashSessionSchedulesUseCase: GetCashSessionSchedulesUseCase,
    private val saveCashSessionScheduleUseCase: SaveCashSessionScheduleUseCase,
    private val deleteCashSessionScheduleUseCase: DeleteCashSessionScheduleUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CashSessionSettingsViewModel(
            getCashSessionSchedulesUseCase = getCashSessionSchedulesUseCase,
            saveCashSessionScheduleUseCase = saveCashSessionScheduleUseCase,
            deleteCashSessionScheduleUseCase = deleteCashSessionScheduleUseCase
        ) as T
    }
}

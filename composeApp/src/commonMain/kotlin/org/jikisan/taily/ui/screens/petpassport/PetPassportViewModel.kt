package org.jikisan.taily.ui.screens.petpassport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jikisan.taily.domain.model.enum.FilterType
import org.jikisan.taily.domain.pet.PetRepository
import org.jikisan.taily.model.pet.Schedule
import org.jikisan.taily.ui.components.now
import org.jikisan.taily.ui.uistates.PassportUIState
import org.jikisan.taily.util.sortPassportDateTime

class PetPassportViewModel(private val repository: PetRepository): ViewModel() {

    private val _uiState = MutableStateFlow(PassportUIState(isLoading = true, errorMessage = null))
    val uiState: StateFlow<PassportUIState> = _uiState.asStateFlow()

    fun loadPetPassport(petId: String) {
        viewModelScope.launch {
            repository.getPetDetails(petId)
                .catch {
                    _uiState.value = _uiState.value.copy(errorMessage = "Failed to load pet schedules")
                }
                .collect { pet ->
                    val schedulesList = sortPassportDateTime(pet)// Sort by datetime

                    _uiState.value = _uiState.value.copy(
                        pet = pet,
                        schedules = schedulesList,
                        isLoading = false)
                }
        }
    }

    fun getTodayAndUpcomingReminders(): List<Schedule>? {
        return uiState.value.schedules?.let {
            filterSchedules(
                schedules = it,
                filterType = FilterType.UPCOMING
            )
        }
    }

    fun getHistoryReminders(): List<Schedule>? {
        return uiState.value.schedules?.let {
            filterSchedules(
                schedules = it,
                filterType = FilterType.HISTORY
            )
        }
    }

    fun filterSchedules(schedules: List<Schedule>, filterType: FilterType): List<Schedule>{

        return schedules.filter { schedule ->
            try {
                val scheduleInstant = Instant.parse(schedule.schedDateTime)
                val scheduleDate = scheduleInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date

                val isSelectedDate = when (filterType) {
                    FilterType.UPCOMING -> scheduleDate >= LocalDate.now()
                    FilterType.HISTORY -> scheduleDate < LocalDate.now()
                    FilterType.ALL -> true
                }

                isSelectedDate

            } catch (e: Exception) {
                Napier.e("Error parsing schedule dateTime: ${schedule.schedDateTime}", e)
                false
            }
        }
    }
}


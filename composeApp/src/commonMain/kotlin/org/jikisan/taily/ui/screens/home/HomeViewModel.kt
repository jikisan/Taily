package org.jikisan.taily.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidspark.androidapp.ui.theme.Blue
import com.vidspark.androidapp.ui.theme.SoftGreen
import com.vidspark.androidapp.ui.theme.SoftOrange
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
import org.jikisan.cmpecommerceapp.util.ApiRoutes.TAG
import org.jikisan.taily.domain.home.HomeRepository
import org.jikisan.taily.domain.model.Reminder
import org.jikisan.taily.domain.model.ReminderList
import org.jikisan.taily.domain.model.enum.ReminderType
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.components.EventDot
import org.jikisan.taily.ui.uistates.HomeUIState

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        getReminders()
    }

    fun getReminders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)


            homeRepository.getPetsByUserId()
                .catch { throwable ->
                    Napier.v("$TAG Load All Pets By User ID Failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to load reminders"
                    )
                }
                .collect { pets ->
                    Napier.v("$TAG Load All Pets By User ID Success")

                    val remindersList = sortDateTime(pets) // Sort by datetime

                    _uiState.value = _uiState.value.copy(
                        pets = pets,
                        reminders = remindersList,
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = null
                    )
                }
        }
    }

    fun refreshReminders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            homeRepository.getPetsByUserId()
                .catch { throwable ->
                    Napier.v("$TAG Refresh Load All Pets failed")
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        errorMessage = throwable.message ?: "Unknown error occurred"
                    )
                }
                .collect { pets ->
                    Napier.v("$TAG Refresh Load All Pets success")

                    val remindersList = sortDateTime(pets) // Sort by datetime

                    _uiState.value = _uiState.value.copy(
                        pets = pets,
                        reminders = remindersList,
                        isRefreshing = false,
                        errorMessage = null
                    )
                }
        }
    }

    fun filterReminders(selectedDate: LocalDate): List<ReminderList> {

        return uiState.value.reminders.filter { reminderList ->
            try {
                val reminderInstant = Instant.parse(reminderList.dateTime)
                val reminderDate =
                    reminderInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                val isSelectedDate = reminderDate == selectedDate

                isSelectedDate

            } catch (e: Exception) {
                Napier.e("Error parsing reminder dateTime: ${reminderList.dateTime}", e)
                false
            }
        }
    }

    fun mapEventDots(reminders: List<ReminderList>): Map<LocalDate, List<EventDot>> {
        val eventMap = mutableMapOf<LocalDate, MutableSet<ReminderType>>()

        reminders.forEach { reminderList ->
            try {
                val reminderInstant = Instant.parse(reminderList.dateTime)
                val reminderDate =
                    reminderInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date

                // Get existing reminder types for this date or create new set
                val existingTypes = eventMap.getOrPut(reminderDate) { mutableSetOf() }

                // Add all reminder types from this reminder list
                reminderList.reminders.forEach { reminder ->
                    existingTypes.add(reminder.reminderType)
                }
            } catch (e: Exception) {
                Napier.e("Error parsing reminder dateTime: ${reminderList.dateTime}", e)
            }
        }

        // Convert to EventDots
        return eventMap.mapValues { (_, reminderTypes) ->
            reminderTypes.map { reminderType ->
                val color = when (reminderType) {
                    ReminderType.PASSPORT -> Blue
                    ReminderType.PETCARE -> SoftGreen
                    ReminderType.MEDICAL -> SoftOrange
                }
                EventDot(color = color, count = 1)
            }
        }
    }

    private fun sortDateTime(pets: List<Pet>): List<ReminderList> = pets.flatMap { pet ->
        // Collect all reminders for this pet
        val allReminders = mutableListOf<Pair<String, Reminder>>()

        // Add passport schedules
        pet.passport?.schedules?.forEach { schedule ->
            allReminders.add(
                schedule.schedDateTime to Reminder(
                    id = schedule.id,
                    type = schedule.vaccineType,
                    reminderType = ReminderType.PASSPORT,
                    petId = pet.id,
                    petName = pet.name
                )
            )
        }

        // Add pet care
        pet.petCare?.forEach { care ->
            allReminders.add(
                care.groomingDateTime to Reminder(
                    id = care.id,
                    type = care.careType,
                    reminderType = ReminderType.PETCARE,
                    petId = pet.id,
                    petName = pet.name
                )
            )
        }

        // Add medical records
        pet.medicalRecords?.forEach { medical ->
            allReminders.add(
                medical.medicalDateTime to Reminder(
                    id = medical.id,
                    type = medical.medicalType,
                    reminderType = ReminderType.MEDICAL,
                    petId = pet.id,
                    petName = pet.name
                )
            )
        }

        // Group by exact datetime and create ReminderList
        allReminders.groupBy { it.first }
            .map { (dateTime, reminders) ->
                ReminderList(
                    dateTime = dateTime,
                    reminders = reminders.map { it.second }
                )
            }
    }.sortedBy { it.dateTime }

}
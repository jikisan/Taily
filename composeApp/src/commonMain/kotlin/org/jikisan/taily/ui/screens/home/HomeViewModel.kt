package org.jikisan.taily.ui.screens.home

import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.jikisan.cmpecommerceapp.util.ApiRoutes.TAG
import org.jikisan.taily.domain.home.HomeRepository
import org.jikisan.taily.domain.model.Reminder
import org.jikisan.taily.domain.model.ReminderList
import org.jikisan.taily.domain.model.ReminderType
import org.jikisan.taily.model.pet.Passport
import org.jikisan.taily.ui.uistates.HomeUIState

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    init {
        loadPets()
    }

    fun loadPets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)


            homeRepository.getPets()
                .catch { throwable ->
                    Napier.v("$TAG Load All Pets Failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unknown error occurred"
                    )
                }
                .collect { pets ->
                    Napier.v("$TAG Load All Pets")

                    val remindersList = pets.flatMap { pet ->
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
                    }.sortedBy { it.dateTime } // Sort by datetime

                    _uiState.value = _uiState.value.copy(
                        pets = pets,
                        reminders = remindersList,
                        isRefreshing = false,
                        errorMessage = null
                    )
                }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            homeRepository.getPets()
                .catch { throwable ->
                    Napier.v("$TAG Refresh Load All Pets failed")
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        errorMessage = throwable.message ?: "Unknown error occurred"
                    )
                }
                .collect { pets ->
                    Napier.v("$TAG Refresh Load All Pets success")

                    val remindersList = pets.flatMap { pet ->
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
                    }.sortedBy { it.dateTime } // Sort by datetime

                    _uiState.value = _uiState.value.copy(
                        pets = pets,
                        reminders = remindersList,
                        isRefreshing = false,
                        errorMessage = null
                    )
                }
        }
    }


}
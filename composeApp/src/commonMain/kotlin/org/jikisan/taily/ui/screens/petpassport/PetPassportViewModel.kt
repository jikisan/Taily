package org.jikisan.taily.ui.screens.petpassport

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.jikisan.taily.domain.pet.PetRepository
import org.jikisan.taily.ui.uistates.PassportUIState
import org.jikisan.taily.viewmodel.PetApiService

class PetPassportViewModel(private val repository: PetRepository): ViewModel() {

    private val _uiState = MutableStateFlow(PassportUIState(isLoading = true))
    val uiState: StateFlow<PassportUIState> = _uiState.asStateFlow()

    fun loadPetPassport(petId: String) {
        viewModelScope.launch {
            repository.getPetDetails(petId)
                .catch {
                    _uiState.value = _uiState.value.copy(errorMessage = "Failed to load pet schedules")
                }
                .collect { pet ->
                    _uiState.value = _uiState.value.copy(
                        pet = pet,
                        schedule = pet.passport.schedules,
                        isLoading = false)
                }
        }
    }
}
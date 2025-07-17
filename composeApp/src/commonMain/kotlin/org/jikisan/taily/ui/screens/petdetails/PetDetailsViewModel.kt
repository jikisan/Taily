package org.jikisan.taily.ui.screens.petdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jikisan.taily.domain.petdetails.PetDetailsRepository
import org.jikisan.taily.ui.uistates.PetDetailUIState

class PetDetailsViewModel(private val repository: PetDetailsRepository) : ViewModel() {

    val _uiState = MutableStateFlow( PetDetailUIState(isLoading = true) )
    val uiState : StateFlow<PetDetailUIState> = _uiState.asStateFlow()

    init {

    }

    fun loadPetDetails(petId: String) {
        viewModelScope.launch {
            repository.loadPetDetails(petId).collect { pet ->
                _uiState.value = _uiState.value.copy(pet = pet, isLoading = false)
            }
        }
    }
}
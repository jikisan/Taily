package org.jikisan.taily.ui.screens.pet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jikisan.cmpecommerceapp.util.ApiRoutes.TAG
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.pet.PetRepository
import org.jikisan.taily.ui.uistates.PetUIState


class PetViewModel(
    private val petRepository: PetRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(PetUIState())
    val uiState: StateFlow<PetUIState> = _uiState.asStateFlow()

    init {
        loadPets()
    }

    fun loadPets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            petRepository.getPetsByUserId()
                .catch { throwable ->
                    Napier.v("$TAG Load All Pets Failed")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unable to load Pets. Please try again."
                    )
                }
                .collectLatest { pets ->
                    Napier.v("$TAG Load All Pets")
                    _uiState.value = _uiState.value.copy(
                        pets = pets,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }

    fun refreshPets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            petRepository.getPetsByUserId()
                .catch { throwable ->
                    Napier.v("$TAG Refresh Load All Pets")
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        errorMessage = throwable.message ?: "Unable to load Pets. Please try again."
                    )
                }
                .collect { pets ->
                    _uiState.value = _uiState.value.copy(
                        pets = pets,
                        isRefreshing = false,
                        errorMessage = null
                    )
                }
        }
    }


}
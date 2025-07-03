package org.jikisan.taily.ui.screens.home

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
import org.jikisan.taily.domain.home.HomeRepository
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

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)

            homeRepository.getPets()
                .catch { throwable ->
                    Napier.v("$TAG Refresh Load All Pets")
                    _uiState.value = _uiState.value.copy(
                        isRefreshing = false,
                        errorMessage = throwable.message ?: "Unknown error occurred"
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
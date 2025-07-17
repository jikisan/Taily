package org.jikisan.taily.ui.screens.petdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import org.jikisan.cmpecommerceapp.util.ApiRoutes.TAG
import org.jikisan.taily.data.remote.supabase.storage.StorageManager
import org.jikisan.taily.domain.petdetails.PetDetailsRepository
import org.jikisan.taily.ui.uistates.PetDetailUIState

class PetDetailsViewModel(
    private val repository: PetDetailsRepository,
    private val storage: StorageManager
) : ViewModel() {

    val _uiState = MutableStateFlow(PetDetailUIState(isLoading = true))
    val uiState: StateFlow<PetDetailUIState> = _uiState.asStateFlow()

    init {

    }

    fun loadPetDetails(petId: String) {
        viewModelScope.launch {
            repository.loadPetDetails(petId).collect { pet ->
                _uiState.value = _uiState.value.copy(pet = pet, isLoading = false)
            }
        }
    }

    fun deletePet(
        petId: String,
        onSuccess: () -> Unit
    ) {
        val fileName = _uiState.value.pet?.photo?.name
        val ownerId = _uiState.value.pet?.ownerId

        viewModelScope.launch {
            // Initial state: deleting started
            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                errorMessage = null,
                deleteSuccessMessage = null,
                isDeletingSuccess = false
            )

            try {
                // 1️⃣ Delete pet in REST server
                val deletePetResult = repository.deletePet(petId).single()
                Napier.i("$TAG Pet deleted in REST server")

                // 2️⃣ Delete photo in Supabase if it exists
                if (!fileName.isNullOrBlank() && !ownerId?.userId.isNullOrBlank()) {
                    val storageResult = storage.deleteFile(
                        userId = ownerId.userId,
                        fileName = fileName
                    )

                    if (storageResult.isFailure) {
                        Napier.e("$TAG Failed to delete photo in Supabase")
                        // Optional: still proceed since DB is already clean
                    } else {
                        Napier.i("$TAG Photo deleted in Supabase")
                    }
                }

                // 3️⃣ Update UI state with success
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    isDeletingSuccess = true,
                    deleteSuccessMessage = deletePetResult.message
                )

                delay(2000)
                onSuccess()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    isDeletingSuccess = false,
                    errorMessage = e.message ?: "Failed to delete pet"
                )
                Napier.e("$TAG VM Error deleting pet: ${e.message}")
            }
        }
    }


}
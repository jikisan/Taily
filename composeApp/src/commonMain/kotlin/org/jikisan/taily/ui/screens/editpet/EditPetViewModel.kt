package org.jikisan.taily.ui.screens.editpet

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.LocaleList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jikisan.cmpecommerceapp.util.ApiRoutes.TAG
import org.jikisan.taily.data.local.mockdata.MockData.MOCK_USERID
import org.jikisan.taily.data.remote.supabase.storage.StorageManager
import org.jikisan.taily.domain.model.Photo
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.enum.GenderType
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.domain.pet.PetRepository
import org.jikisan.taily.model.pet.Identifiers
import org.jikisan.taily.ui.uistates.EditPetUIState

class EditPetViewModel(
    private val repository: PetRepository,
    private val storageManager: StorageManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditPetUIState())
    val uiState: StateFlow<EditPetUIState> = _uiState.asStateFlow()

    fun loadPetDetails(petId: String) {
        viewModelScope.launch {
            repository.getPetDetails(petId).collect { pet ->
                _uiState.value = _uiState.value.copy(pet = pet, isLoading = false)
                _uiState.value = EditPetUIState(pet = pet)
            }
        }
    }

    private fun updatePet(newPet: Pet) {
        _uiState.value = _uiState.value.copy(pet = newPet)
        Napier.i("$TAG Pet Updated: ${_uiState.value.pet}")
    }

    fun updatePetPhotoByteArray(newImageByteArray: ByteArray) {
        val metadata = _uiState.value.imageByteArray
        metadata.let {
            _uiState.value = _uiState.value.copy(imageByteArray = newImageByteArray)
        }

    }

    fun updateName(name: String) {
        val pet = _uiState.value.pet
        pet?.let {
            updatePet(it.copy(name = name.capitalize(LocaleList.current)))
        }
    }

    fun updatePhoto(photo: Photo) {
        val pet = _uiState.value.pet
        pet?.let {
            updatePet(newPet = it.copy(photo = photo))
        }
    }

    fun updatePhotoImageBitmap(image: ImageBitmap) {
        val pet = _uiState.value.pet
        pet?.let {
            updatePet(it.copy(imageBitmap = image))
        }
    }

    fun updateGender(gender: GenderType) {
        val pet = _uiState.value.pet
        pet?.let {
            updatePet(it.copy(gender = gender.toString()))
        }
    }

    fun updateDateOfBirth(date: String) {
        val pet = _uiState.value.pet
        pet?.let {
            updatePet(it.copy(dateOfBirth = date))
        }
    }

    fun updatePetType(type: String) {
        val pet = _uiState.value.pet
        pet?.let {
            updatePet(it.copy(petType = type))
        }
    }

    fun updateBreed(breed: String) {
        val pet = _uiState.value.pet
        pet?.let {
            updatePet(it.copy(breed = breed))
        }
    }

    fun updateWeight(weight: Weight) {
        val pet = _uiState.value.pet
        pet?.let {
            updatePet(it.copy(weight = weight))
        }
    }

    fun updateIdentifiers(identifiers: Identifiers) {
        val pet = _uiState.value.pet
        pet?.let {
            updatePet(newPet = it.copy(identifiers = identifiers))
        }
    }

    fun submitPet() {
        viewModelScope.launch {
            try {
                // Set loading state externally in composable, if needed
                val imageByteArray = _uiState.value.imageByteArray
                val pet = _uiState.value.pet


                if (pet == null || imageByteArray == null) {
                    failSubmission(
                        error = "Pet information or image bytes are missing.",
                        logmessage = "Pet is null or image bytes are null."
                    )
                    return@launch
                }

                val fileName = pet.photo.name


                val uploadResult = storageManager.uploadFile(
                    userId = MOCK_USERID,
                    fileData = imageByteArray,
                    fileName = fileName
                )

                if (uploadResult.isSuccess) {

                    uploadResult.onSuccess { photoUrl ->
                        val updatedPet =
                            pet.copy(photo = pet.photo.copy(name = fileName, url = photoUrl))

                        try {

                            val createPetResult = repository.createPet(updatedPet)

                            createPetResult.onSuccess {
                                updateIsSubmittingSuccess(true)
                            }
                            createPetResult.onFailure {
                                failSubmission(
                                    error = "Failed to save Pet data. Check connection.",
                                    logmessage = it.message
                                )
                            }

                        } catch (e: Exception) {

                            failSubmission(
                                error = "Failed to save Pet data. Check connection.",
                                logmessage = e.message
                            )
                        }
                    }
                } else {
                    failSubmission(
                        error = "Failed to save Pet data. Check connection.",
                        logmessage = uploadResult.exceptionOrNull()?.message,
                    )
                }
            } catch (e: Exception) {
                failSubmission(
                    error = "Failed to save Pet data. Check connection.",
                    logmessage = e.message,
                )
            }
        }
    }

    private fun failSubmission(error: String?, logmessage: String?) {
        Napier.e("$TAG $logmessage")
        updateErrorMessage(error)
        updateIsSubmitting(false)
        updateIsSubmittingSuccess(false)
    }


    fun updateErrorMessage(message: String?) {
        _uiState.value = _uiState.value.copy(errorMessage = message)
        Napier.e("$TAG Update Error Message: $message")
    }

    fun updateIsSubmitting(isSubmitting: Boolean) {
        _uiState.value = _uiState.value.copy(isSubmitting = isSubmitting)
    }

    fun updateIsSubmittingSuccess(success: Boolean) {
        updateIsSubmitting(false)
        _uiState.value = _uiState.value.copy(isSubmittingSuccess = success)
        if (success) {
            Napier.i("$TAG Pet submitted successfully")
        }
    }
}
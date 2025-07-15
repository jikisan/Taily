package org.jikisan.taily.ui.screens.addpet

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
import org.jikisan.taily.data.local.mockdata.MockData.MOCK_USER_EMAIL
import org.jikisan.taily.data.local.mockdata.MockData.MOCK_USER_NAME
import org.jikisan.taily.data.remote.supabase.storage.StorageManager
import org.jikisan.taily.domain.addpet.AddPetRepository
import org.jikisan.taily.domain.model.Photo
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.enum.GenderType
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.Identifiers
import org.jikisan.taily.model.pet.Owner
import org.jikisan.taily.model.pet.Passport
import org.jikisan.taily.ui.screens.addpet.PetConstants.WEIGHT_UNITS
import org.jikisan.taily.ui.uistates.AddPetUIState


class AddPetViewModel(
    private val storageManager: StorageManager,
    private val repository: AddPetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPetUIState())
    val uiState: StateFlow<AddPetUIState> = _uiState.asStateFlow()

    // Track the upload result for navigation/feedback
    val uploadSuccess = MutableStateFlow<Boolean?>(null)

    // --- Use a blank Pet with correct defaults ---
    private fun blankPet() = Pet(
        id = "",
        name = "",
        petType = "",
        breed = "",
        dateOfBirth = "",
        gender = "",
        photo = Photo(
            name = "",
            url = ""
        ),
        weight = Weight(
            value = 0.0,
            unit = WEIGHT_UNITS[0]
        ),
        ownerId = Owner(
            email = MOCK_USER_EMAIL,
            fullName = MOCK_USER_NAME,
            id = MOCK_USERID,
            userId = MOCK_USERID
        ),
        identifiers = Identifiers(
            allergies = emptyList(),
            clipLocation = "",
            colorMarkings = "",
            isNeuteredOrSpayed = false,
            microchipLocation = "",
            microchipNumber = "",
            size = ""
        ),
        passport = Passport(schedules = emptyList()),
        medicalRecords = emptyList(),
        petCare = emptyList(),
        petIds = emptyList(),
        createdAt = "",
        updatedAt = "",
    )


    init {
        // Initialize with a blank pet if needed
        _uiState.value = AddPetUIState(pet = blankPet())
    }

    // Helper to update Pet state within uiState
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

    fun updateSize(size: String) {
        val pet = _uiState.value.pet
        pet?.let {
            val updatedIdentifiers = it.identifiers.copy(size = size)
            updatePet(it.copy(identifiers = updatedIdentifiers))
        }
    }

    fun updateColorMarkings(markings: String) {
        val pet = _uiState.value.pet
        pet?.let {
            val updatedIdentifiers = it.identifiers.copy(colorMarkings = markings)
            updatePet(it.copy(identifiers = updatedIdentifiers))
        }
    }

    fun updateMicrochipNumber(number: String) {
        val pet = _uiState.value.pet
        pet?.let {
            val updatedIdentifiers = it.identifiers.copy(microchipNumber = number)
            updatePet(it.copy(identifiers = updatedIdentifiers))
        }
    }

    fun updateMicrochipLocation(location: String) {
        val pet = _uiState.value.pet
        pet?.let {
            val updatedIdentifiers = it.identifiers.copy(microchipLocation = location)
            updatePet(it.copy(identifiers = updatedIdentifiers))
        }
    }

    fun updateClipLocation(location: String) {
        val pet = _uiState.value.pet
        pet?.let {
            val updatedIdentifiers = it.identifiers.copy(clipLocation = location)
            updatePet(it.copy(identifiers = updatedIdentifiers))
        }
    }

    fun updateNeuteredSpayed(status: Boolean) {
        val pet = _uiState.value.pet
        pet?.let {
            val updatedIdentifiers = it.identifiers.copy(isNeuteredOrSpayed = status)
            updatePet(it.copy(identifiers = updatedIdentifiers))
        }
    }

    fun updateAllergies(allergies: List<String>) {
        val pet = _uiState.value.pet
        pet?.let {
            val updatedIdentifiers = it.identifiers.copy(allergies = allergies)
            updatePet(it.copy(identifiers = updatedIdentifiers))
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
                    val error = "Pet information or image bytes are missing."
                    Napier.e("$TAG $error")
                    _uiState.value = _uiState.value.copy(errorMessage = error)
                    uploadSuccess.value = false
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
                        // 2. Update the pet's photo with the returned URL
                        val updatedPet = pet.copy(photo = pet.photo.copy(name = fileName, url = photoUrl))

                        // 3. Call repository to create the Pet
                        try {
                            repository.createPet(updatedPet)
                            // 4. Update the UI state and notify success
                            _uiState.value = _uiState.value.copy(pet = updatedPet)
                            uploadSuccess.value = true
                        } catch (e: Exception) {
                            val errMsg = "Failed to save Pet data: ${e.message}"
                            Napier.e("$TAG $errMsg")
                            _uiState.value = _uiState.value.copy(errorMessage = errMsg)
                            uploadSuccess.value = false
                        }
                    }
                } else {
                    Napier.e("$TAG Upload Pet Profile Photo Failed, Error: ${uploadResult.exceptionOrNull()?.message}")
                    _uiState.value =
                        _uiState.value.copy(errorMessage = uploadResult.exceptionOrNull()?.message)
                    uploadSuccess.value = false
                }
            } catch (e: Exception) {
                Napier.e("$TAG Upload Pet Profile Photo Exception: ${e.message}")
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
                uploadSuccess.value = false
            }
        }
    }

    fun displayPet() {
        println("Pet: ${_uiState.value.pet}")
    }

    fun updateErrorMessage(message: String) {
        _uiState.value = _uiState.value.copy(errorMessage = message)
        Napier.e("$TAG Update Error Message: $message")
    }


}
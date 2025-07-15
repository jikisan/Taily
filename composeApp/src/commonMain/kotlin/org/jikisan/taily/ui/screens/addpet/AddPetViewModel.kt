package org.jikisan.taily.ui.screens.addpet

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jikisan.cmpecommerceapp.util.ApiRoutes.TAG
import org.jikisan.taily.data.local.mockdata.MockData
import org.jikisan.taily.data.remote.supabase.storage.StorageManager
import org.jikisan.taily.domain.addpet.AddPetRepository
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.enum.GenderType
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.Identifiers
import org.jikisan.taily.model.pet.Owner
import org.jikisan.taily.model.pet.Passport
import org.jikisan.taily.ui.screens.addpet.PetConstants.WEIGHT_UNITS
import org.jikisan.taily.ui.uistates.AddPetUIState


class AddPetViewModel(private val storageManager: StorageManager, private val repository: AddPetRepository) : ViewModel() {

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
        photoUrl = "",
        weight = Weight(
            value = 0.0,
            unit = WEIGHT_UNITS[0]
        ),
        ownerId = Owner(email = "kylesan@gmail.com", fullName = "Kyle Santerna", id = MockData.MOCK_USERID, userId = MockData.MOCK_USERID),
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
            updatePet(it.copy(name = name))
        }
    }

    fun updatePhotoUrl(url: String) {
        val pet = _uiState.value.pet
        pet?.let {
            updatePet(it.copy(photoUrl = url))
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
            Napier.i("$TAG Pet Weight Updated: ${_uiState.value.pet?.weight}")
            println("$TAG Pet Weight Updated: ${_uiState.value.pet?.weight}")
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
            Napier.i("$TAG Pet Identifiers Updated: ${_uiState.value.pet?.identifiers}")
            println("Pet Identifiers Updated: ${_uiState.value.pet?.identifiers.toString()}")
        }
    }

    fun uploadPetProfilePhoto() {
        viewModelScope.launch {
            try {
                // Set loading state externally in composable, if needed
                val imageByteArray = _uiState.value.imageByteArray

                val result = storageManager.uploadFile(
                    userId = MockData.MOCK_USERID,
                    fileData = imageByteArray,
                )

                if (result.isSuccess) {
                    result.onSuccess { url ->
                        updatePhotoUrl(url)
                        println("Pet Identifiers Updated: ${_uiState.value.pet.toString()}")
                        _uiState.value.pet?.let { pet ->
                            repository.createPet(pet)
                        }
                        uploadSuccess.value = true
                    }
                } else {
                    Napier.e("$TAG Upload Pet Profile Photo Failed, Error: ${result.exceptionOrNull()?.message}")
                    _uiState.value = _uiState.value.copy(errorMessage = result.exceptionOrNull()?.message)
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



}
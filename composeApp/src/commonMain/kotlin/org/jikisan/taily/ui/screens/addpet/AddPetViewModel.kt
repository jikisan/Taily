package org.jikisan.taily.ui.screens.addpet

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
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.Identifiers
import org.jikisan.taily.model.pet.Owner
import org.jikisan.taily.model.pet.Passport
import org.jikisan.taily.ui.uistates.AddPetUIState


class AddPetViewModel(private val storageManager: StorageManager) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPetUIState())
    val uiState: StateFlow<AddPetUIState> = _uiState.asStateFlow()

    // --- Use a blank Pet with correct defaults ---
    private fun blankPet() = Pet(
        id = "",
        name = "",
        petType = "",
        breed = "",
        dateOfBirth = "",
        gender = "",
        photoUrl = "",
        weight = Weight("kg", 0.0),
        ownerId = Owner(email = "", fullName = "", id = "", userId = ""),
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

    fun updateGender(gender: String) {
        val pet = _uiState.value.pet
        pet?.let {
            updatePet(it.copy(gender = gender))
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


    fun uploadPetProfilePhoto() {
        viewModelScope.launch {

            val imageByteArray = _uiState.value.imageByteArray

            imageByteArray.let { byteArray ->
                val result = storageManager.uploadFile(
                    userId = MockData.MOCK_USERID,
                    fileData = byteArray,
                )

                when {
                    result.isSuccess -> {
                        updatePhotoUrl(result.toString())
                    }

                    result.isFailure -> {
                        Napier.e("$TAG Upload Pet Profile Photo Failed, Error: ${result.exceptionOrNull()?.message}")
                    }
                }
            }


        }

    }


}
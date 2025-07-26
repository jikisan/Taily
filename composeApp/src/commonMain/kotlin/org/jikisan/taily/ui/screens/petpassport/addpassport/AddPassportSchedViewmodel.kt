package org.jikisan.taily.ui.screens.petpassport.addpassport

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jikisan.cmpecommerceapp.util.ApiRoutes.TAG
import org.jikisan.taily.data.remote.supabase.storage.StorageManager
import org.jikisan.taily.domain.model.Photo
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.domain.pet.PetRepository
import org.jikisan.taily.model.pet.Given
import org.jikisan.taily.model.pet.Schedule
import org.jikisan.taily.ui.screens.addpet.PetConstants.WEIGHT_UNITS
import org.jikisan.taily.ui.uistates.AddPassportSchedUIState

class AddPassportSchedViewmodel(
    private val repository: PetRepository,
    private val storageManager: StorageManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPassportSchedUIState(isLoading = true, isSubmitting = false, isSubmittingSuccess = false))
    val uiState: StateFlow<AddPassportSchedUIState> = _uiState.asStateFlow()

    private fun blankSched() = Schedule(
        given = Given(
            isGiven = false,
            type = "",
            dateTime = "",
            proofPhoto = Photo(
                name = "",
                url = "",
            )
        ),
        hospital = "",
        notes = "",
        schedDateTime = "",
        vaccineType = "",
        vet = "",
        weight = Weight(
            value = 0.0,
            unit = WEIGHT_UNITS[0]
        ),
        imageBitmap = null
    )

    init {
        _uiState.value = _uiState.value.copy( sched = blankSched() )
    }

    private fun updateSched(newSched: Schedule) {
        _uiState.value = _uiState.value.copy(sched = newSched)
        Napier.i("$TAG Sched Updated: ${_uiState.value.sched}")
    }

    fun updateHospital(newHospital: String) {
        val sched = _uiState.value.sched
        sched?.let { currentSched ->
            updateSched(currentSched.copy(hospital = newHospital))
        }
    }

    fun updateNotes(newNotes: String) {
        val sched = _uiState.value.sched
        sched?.let { currentSched ->
            updateSched(currentSched.copy(notes = newNotes))
        }
    }

    fun updateSchedDateTime(newDateTime: String) {
        val sched = _uiState.value.sched
        sched?.let { currentSched ->
            updateSched(currentSched.copy(schedDateTime = newDateTime))
        }
    }

    fun updateVaccineType(newVaccineType: String) {
        val sched = _uiState.value.sched
        sched?.let { currentSched ->
            updateSched(currentSched.copy(vaccineType = newVaccineType))
        }
    }

    fun updateVet(newVet: String) {
        val sched = _uiState.value.sched
        sched?.let { currentSched ->
            updateSched(currentSched.copy(vet = newVet))
        }
    }

    fun updateWeightValue(newValue: Double) {
        val sched = _uiState.value.sched
        sched?.let { currentSched ->
            val weight = currentSched.weight ?: Weight(0.0, "")
            updateSched(currentSched.copy(weight = weight.copy(value = newValue)))
        }
    }

    fun updateWeightUnit(newUnit: String) {
        val sched = _uiState.value.sched
        sched?.let { currentSched ->
            val weight = currentSched.weight ?: Weight(0.0, "")
            updateSched(currentSched.copy(weight = weight.copy(unit = newUnit)))
        }
    }

    fun updateGivenType(newType: String) {
        val sched = _uiState.value.sched
        sched?.let { currentSched ->
            updateSched(currentSched.copy(given = currentSched.given.copy(type = newType)))
        }
    }

    fun updateGivenDateTime(newDateTime: String) {
        val sched = _uiState.value.sched
        sched?.let { currentSched ->
            updateSched(currentSched.copy(given = currentSched.given.copy(dateTime = newDateTime)))
        }
    }

    fun updateGivenProofPhoto(newPhoto: Photo) {
        val sched = _uiState.value.sched
        sched?.let { currentSched ->
            updateSched(currentSched.copy(given = currentSched.given.copy(proofPhoto = newPhoto)))
        }
    }

    fun updatePetPhotoByteArray(newImageByteArray: ByteArray) {
        val metadata = _uiState.value.imageByteArray
        metadata.let {
            _uiState.value = _uiState.value.copy(imageByteArray = newImageByteArray)
        }

    }

    fun updatePhotoImageBitmap(image: ImageBitmap) {
        val sched = _uiState.value.sched
        sched?.let {
            updateSched(it.copy(imageBitmap = image))
        }
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
            Napier.i("$TAG Pet passport sched submitted successfully")
        }
    }
}
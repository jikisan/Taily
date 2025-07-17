package org.jikisan.taily.ui.uistates

import org.jikisan.taily.domain.model.Reminder
import org.jikisan.taily.domain.model.ReminderList
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.MedicalRecord
import org.jikisan.taily.model.pet.Passport
import org.jikisan.taily.model.pet.PetCare
import org.jikisan.taily.model.pet.Schedule

data class HomeUIState(
    val pets: List<Pet> = emptyList(),
    val reminders: List<ReminderList> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false
)

data class PetUIState(
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false
)

data class AddPetUIState(
    val pet: Pet? = null,
    val imageByteArray: ByteArray = byteArrayOf(),
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val isSubmittingSuccess: Boolean = false,
    val errorMessage: String? = null,
)

data class PetDetailUIState(
    val pet: Pet? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
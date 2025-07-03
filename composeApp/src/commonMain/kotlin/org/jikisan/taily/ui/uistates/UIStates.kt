package org.jikisan.taily.ui.uistates

import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.MedicalRecord
import org.jikisan.taily.model.pet.Passport
import org.jikisan.taily.model.pet.PetCare
import org.jikisan.taily.model.pet.Schedule

data class HomeUIState(
    val pets: List<Pet> = emptyList(),
    val schedules: List<Schedule> = emptyList(),
    val petCares: List<PetCare> = emptyList(),
    val medRecords: List<MedicalRecord> = emptyList(),
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
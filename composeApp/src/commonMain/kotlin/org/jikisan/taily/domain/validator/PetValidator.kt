package org.jikisan.taily.domain.validator

import org.jikisan.taily.domain.model.pet.Pet

fun Pet.validate(currentPage: Int): PetValidationResult {
    return PetValidationResult(
        nameError = if (name.isBlank() && currentPage == 0)
            "Pet name is required" else null,

        photoUrlError = when {
            photo.name.isBlank() && currentPage == 1 ->
                "Pet photo is required"
            else -> null
        },

        genderError = if (gender.isBlank() && currentPage == 2)
            "Gender is required" else null,

        dateOfBirthError = if (dateOfBirth.isBlank() && currentPage == 3)
            "Date of birth is required" else null,

        petTypeError = if (petType.isBlank() && currentPage == 4)
            "Species is required" else null,

        breedError = if (breed.isBlank() && currentPage == 5)
            "Breed is required" else null,

        weightError = when {
            weight.value <= 0 && currentPage == 6 ->
                "Valid weight is required"
            weight.unit.isBlank() && currentPage == 6 ->
                "Weight unit is required"
            else -> null
        }
    )
}


data class PetValidationResult(
    val nameError: String? = null,
    val petTypeError: String? = null,
    val breedError: String? = null,
    val dateOfBirthError: String? = null,
    val genderError: String? = null,
    val photoUrlError: String? = null,
    val weightError: String? = null,
    val ownerError: String? = null,
) {
    val isValid: Boolean
        get() = listOf(
            nameError,
            petTypeError,
            breedError,
            dateOfBirthError,
            genderError,
            photoUrlError,
            weightError,
            ownerError,
        ).all { it == null }

    val error: String?
        get() = listOf(
            nameError,
            petTypeError,
            breedError,
            dateOfBirthError,
            genderError,
            photoUrlError,
            weightError,
            ownerError,
        ).firstOrNull { it != null }

}

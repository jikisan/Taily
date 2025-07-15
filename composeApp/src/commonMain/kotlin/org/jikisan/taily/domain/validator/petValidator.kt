package org.jikisan.taily.domain.validator

import org.jikisan.taily.domain.model.pet.Pet

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

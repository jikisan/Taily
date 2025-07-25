package org.jikisan.taily.domain.validator

import org.jikisan.taily.model.pet.Schedule

fun Schedule.validate(isPassedDate: Boolean): SchedValidationResult {
    return SchedValidationResult(
        vaccineTypeError = if (vaccineType.isNullOrBlank())
            "Vaccine type is required" else null,

        hospitalError = if (hospital.isNullOrBlank())
            "Hospital name is required" else null,

        schedDateTimeError = if (schedDateTime.isNullOrBlank())
            "Schedule date/time is required" else null,

        notesError = null, // assuming notes are optional, skip validation

        givenTypeError = if (isPassedDate && given?.type.isNullOrBlank())
            "Given type is required" else null,

        givenDateTimeError = if (isPassedDate && given?.dateTime.isNullOrBlank())
            "Given date/time is required" else null,

        givenProofPhotoError = if (isPassedDate && given?.proofPhoto.isNullOrBlank())
            "Proof photo is required" else null,

        weightValueError = if ((weight?.value == null || weight.value <= 0.0))
            "Valid weight is required" else null,

        weightUnitError = if (weight?.unit.isNullOrBlank())
            "Weight unit is required" else null,

        vetError = if (vet.isNullOrBlank())
            "Vet name is required" else null,
    )
}



data class SchedValidationResult(
    val vaccineTypeError: String? = null,
    val hospitalError: String? = null,
    val schedDateTimeError: String? = null,
    val notesError: String? = null,
    val givenTypeError: String? = null,
    val givenDateTimeError: String? = null,
    val givenProofPhotoError: String? = null,
    val weightValueError: String? = null,
    val weightUnitError: String? = null,
    val vetError: String? = null,
) {
    val isValid: Boolean
        get() = listOf(
            vaccineTypeError,
            hospitalError,
            schedDateTimeError,
            notesError,
            givenTypeError,
            givenDateTimeError,
            givenProofPhotoError,
            weightValueError,
            weightUnitError,
            vetError,
        ).all { it == null }

    val error: String?
        get() = listOf(
            vaccineTypeError,
            hospitalError,
            schedDateTimeError,
            notesError,
            givenTypeError,
            givenDateTimeError,
            givenProofPhotoError,
            weightValueError,
            weightUnitError,
            vetError,
        ).firstOrNull { it != null }
}

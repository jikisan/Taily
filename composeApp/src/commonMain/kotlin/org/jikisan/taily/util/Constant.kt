package org.jikisan.cmpecommerceapp.util

object ApiRoutes {


    private const val BASE_URL = "https://site--taily--gqh7yj78q7bk.code.run"
    const val TAG = "[DEBUG]"

    // PETS
    const val PETS = "$BASE_URL/api/pets"
    const val PET_BY_ID = "$PETS/{id}"

    const val ADD_SCHEDULE = "$PETS/{id}/passport/schedules"
    const val UPDATE_SCHEDULE = "$PETS/{id}/passport/schedules/{scheduleId}"
    const val DELETE_SCHEDULE = "$PETS/{id}/passport/schedules/{scheduleId}"

    const val ADD_PET_CARE = "$PETS/{id}/petCare"
    const val UPDATE_PET_CARE = "$PETS/{id}/petCare/{careId}"
    const val DELETE_PET_CARE = "$PETS/{id}/petCare/{careId}"

    const val ADD_MEDICAL_RECORD = "$PETS/{id}/medicalRecords"
    const val UPDATE_MEDICAL_RECORD = "$PETS/{id}/medicalRecords/{recordId}"
    const val DELETE_MEDICAL_RECORD = "$PETS/{id}/medicalRecords/{recordId}"

    const val ADD_PET_ID = "$PETS/{id}/petIds"
    const val UPDATE_PET_ID = "$PETS/{id}/petIds/{petIdRecordId}"
    const val DELETE_PET_ID = "$PETS/{id}/petIds/{petIdRecordId}"

    // USERS
    const val USERS = "$BASE_URL/api/users"
    const val USER_BY_ID = "$USERS/{id}"
    const val USER_BY_EMAIL = "$USERS/email/{email}"
    const val UPDATE_USER_ROLE = "$USERS/{id}/role"
}

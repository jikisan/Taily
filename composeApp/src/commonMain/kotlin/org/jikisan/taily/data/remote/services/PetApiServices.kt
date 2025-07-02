package org.jikisan.taily.viewmodel

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.put
import org.jikisan.cmpecommerceapp.util.ApiRoutes
import org.jikisan.taily.model.pet.PetDTO
import org.jikisan.taily.model.pet.ScheduleDTO

// Exception classes
sealed class ApiException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class NetworkException(message: String, cause: Throwable? = null) : ApiException(message, cause)
    class ServerException(val code: Int, message: String) : ApiException(message)
    class ClientException(val code: Int, message: String) : ApiException(message)
    class UnknownException(message: String, cause: Throwable? = null) : ApiException(message, cause)
}

//class PetApi(private val client: HttpClient) {
//
//    suspend fun fetchAllPets(): List<PetDTO> {
//        return client.get(ApiRoutes.PETS).body()
//    }
//
//}

// Main API Service
class PetApiService(private val client: HttpClient) {

    // PETS
    suspend fun fetchAllPets(): Result<List<PetDTO>> = safeApiCall {
        client.get(ApiRoutes.PETS).body<List<PetDTO>>()
    }

    suspend fun fetchPetById(id: String): Result<PetDTO> = safeApiCall {
        client.get(ApiRoutes.PET_BY_ID.replace("{id}", id)).body<PetDTO>()
    }

    suspend fun createPet(request: CreatePetRequest): Result<PetDTO> = safeApiCall {
        client.post(ApiRoutes.PETS) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<PetDTO>()
    }

    suspend fun updatePet(id: String, request: UpdatePetRequest): Result<PetDTO> = safeApiCall {
        client.put(ApiRoutes.PET_BY_ID.replace("{id}", id)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<PetDTO>()
    }

    suspend fun deletePet(id: String): Result<Unit> = safeApiCall {
        client.delete(ApiRoutes.PET_BY_ID.replace("{id}", id))
    }

    // SCHEDULES
    suspend fun addSchedule(petId: String, request: CreateScheduleRequest): Result<ScheduleDTO> = safeApiCall {
        client.post(ApiRoutes.ADD_SCHEDULE.replace("{id}", petId)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<ScheduleDTO>()
    }

    suspend fun updateSchedule(
        petId: String,
        scheduleId: String,
        request: UpdateScheduleRequest
    ): Result<ScheduleDTO> = safeApiCall {
        client.put(
            ApiRoutes.UPDATE_SCHEDULE
                .replace("{id}", petId)
                .replace("{scheduleId}", scheduleId)
        ) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<ScheduleDTO>()
    }

    suspend fun deleteSchedule(petId: String, scheduleId: String): Result<Unit> = safeApiCall {
        client.delete(
            ApiRoutes.DELETE_SCHEDULE
                .replace("{id}", petId)
                .replace("{scheduleId}", scheduleId)
        )
    }

    // PET CARE
    suspend fun addPetCare(petId: String, request: CreatePetCareRequest): Result<PetCareDTO> = safeApiCall {
        client.post(ApiRoutes.ADD_PET_CARE.replace("{id}", petId)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<PetCareDTO>()
    }

    suspend fun updatePetCare(
        petId: String,
        careId: String,
        request: UpdatePetCareRequest
    ): Result<PetCareDTO> = safeApiCall {
        client.put(
            ApiRoutes.UPDATE_PET_CARE
                .replace("{id}", petId)
                .replace("{careId}", careId)
        ) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<PetCareDTO>()
    }

    suspend fun deletePetCare(petId: String, careId: String): Result<Unit> = safeApiCall {
        client.delete(
            ApiRoutes.DELETE_PET_CARE
                .replace("{id}", petId)
                .replace("{careId}", careId)
        )
    }

    // MEDICAL RECORDS
    suspend fun addMedicalRecord(
        petId: String,
        request: CreateMedicalRecordRequest
    ): Result<MedicalRecordDTO> = safeApiCall {
        client.post(ApiRoutes.ADD_MEDICAL_RECORD.replace("{id}", petId)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<MedicalRecordDTO>()
    }

    suspend fun updateMedicalRecord(
        petId: String,
        recordId: String,
        request: UpdateMedicalRecordRequest
    ): Result<MedicalRecordDTO> = safeApiCall {
        client.put(
            ApiRoutes.UPDATE_MEDICAL_RECORD
                .replace("{id}", petId)
                .replace("{recordId}", recordId)
        ) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<MedicalRecordDTO>()
    }

    suspend fun deleteMedicalRecord(petId: String, recordId: String): Result<Unit> = safeApiCall {
        client.delete(
            ApiRoutes.DELETE_MEDICAL_RECORD
                .replace("{id}", petId)
                .replace("{recordId}", recordId)
        )
    }

    // PET IDS
    suspend fun addPetId(petId: String, request: CreatePetIdRequest): Result<PetIdDTO> = safeApiCall {
        client.post(ApiRoutes.ADD_PET_ID.replace("{id}", petId)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<PetIdDTO>()
    }

    suspend fun updatePetId(
        petId: String,
        petIdRecordId: String,
        request: UpdatePetIdRequest
    ): Result<PetIdDTO> = safeApiCall {
        client.put(
            ApiRoutes.UPDATE_PET_ID
                .replace("{id}", petId)
                .replace("{petIdRecordId}", petIdRecordId)
        ) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<PetIdDTO>()
    }

    suspend fun deletePetId(petId: String, petIdRecordId: String): Result<Unit> = safeApiCall {
        client.delete(
            ApiRoutes.DELETE_PET_ID
                .replace("{id}", petId)
                .replace("{petIdRecordId}", petIdRecordId)
        )
    }

    // USERS
    suspend fun fetchAllUsers(): Result<List<UserDTO>> = safeApiCall {
        client.get(ApiRoutes.USERS).body<List<UserDTO>>()
    }

    suspend fun fetchUserById(id: String): Result<UserDTO> = safeApiCall {
        client.get(ApiRoutes.USER_BY_ID.replace("{id}", id)).body<UserDTO>()
    }

    suspend fun fetchUserByEmail(email: String): Result<UserDTO> = safeApiCall {
        client.get(ApiRoutes.USER_BY_EMAIL.replace("{email}", email)).body<UserDTO>()
    }

    suspend fun createUser(request: CreateUserRequest): Result<UserDTO> = safeApiCall {
        client.post(ApiRoutes.USERS) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<UserDTO>()
    }

    suspend fun updateUser(id: String, request: UpdateUserRequest): Result<UserDTO> = safeApiCall {
        client.put(ApiRoutes.USER_BY_ID.replace("{id}", id)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<UserDTO>()
    }

    suspend fun updateUserRole(id: String, request: UpdateUserRoleRequest): Result<UserDTO> = safeApiCall {
        client.put(ApiRoutes.UPDATE_USER_ROLE.replace("{id}", id)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<UserDTO>()
    }

    suspend fun deleteUser(id: String): Result<Unit> = safeApiCall {
        client.delete(ApiRoutes.USER_BY_ID.replace("{id}", id))
    }

    // Safe API call wrapper with proper error handling
    private suspend inline fun <T> safeApiCall(crossinline apiCall: suspend () -> T): Result<T> {
        return try {
            val result = apiCall()
            Result.success(result)
        } catch (e: Exception) {
            when (e) {
                is ClientRequestException -> {
                    when (e.response.status.value) {
                        in 400..499 -> Result.failure(
                            ApiException.ClientException(
                                e.response.status.value,
                                "Client error: ${e.message}"
                            )
                        )
                        else -> Result.failure(
                            ApiException.UnknownException("Unknown client error", e)
                        )
                    }
                }
                is ServerResponseException -> {
                    Result.failure(
                        ApiException.ServerException(
                            e.response.status.value,
                            "Server error: ${e.message}"
                        )
                    )
                }
                is ConnectTimeoutException, is SocketTimeoutException -> {
                    Result.failure(
                        ApiException.NetworkException("Network timeout", e)
                    )
                }
                is UnresolvedAddressException -> {
                    Result.failure(
                        ApiException.NetworkException("No internet connection", e)
                    )
                }
                else -> {
                    Result.failure(
                        ApiException.UnknownException("Unknown error: ${e.message}", e)
                    )
                }
            }
        }
    }
}

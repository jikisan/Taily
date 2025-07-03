package org.jikisan.taily.viewmodel

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import org.jikisan.cmpecommerceapp.util.ApiRoutes
import org.jikisan.cmpecommerceapp.util.ApiRoutes.TAG
import org.jikisan.taily.data.model.pet.request.UpdatePetRequest
import org.jikisan.taily.model.pet.CreateMedicalRecordRequest
import org.jikisan.taily.model.pet.CreatePetCareRequest
import org.jikisan.taily.model.pet.CreatePetIdRequest
import org.jikisan.taily.model.pet.CreateScheduleRequest
import org.jikisan.taily.model.pet.PetDTO
import org.jikisan.taily.model.pet.UpdateMedicalRecordRequest
import org.jikisan.taily.model.pet.UpdatePetCareRequest
import org.jikisan.taily.model.pet.UpdatePetIdRequest
import org.jikisan.taily.model.pet.UpdateScheduleRequest

// Exception classes
sealed class ApiException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class NetworkException(message: String, cause: Throwable? = null) : ApiException(message, cause)
    class ServerException(val code: Int, message: String) : ApiException(message)
    class ClientException(val code: Int, message: String) : ApiException(message)
    class UnknownException(message: String, cause: Throwable? = null) : ApiException(message, cause)
}


// Main API Service
class PetApiService(private val client: HttpClient) {

    // PETS
    suspend fun fetchAllPets(): Result<List<PetDTO>> = safeApiCall {
        Napier.v("$TAG Fetch All Pets")
        client.get(ApiRoutes.PETS).body<List<PetDTO>>()
    }

    suspend fun fetchPetById(id: String): Result<PetDTO> = safeApiCall {
        client.get(ApiRoutes.PET_BY_ID.replace("{id}", id)).body<PetDTO>()
    }

    suspend fun createPet(request: PetDTO): Result<PetDTO> = safeApiCall {
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
    suspend fun addSchedule(petId: String, request: CreateScheduleRequest): Result<CreateScheduleRequest> = safeApiCall {
        client.post(ApiRoutes.ADD_SCHEDULE.replace("{id}", petId)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<CreateScheduleRequest>()
    }

    suspend fun updateSchedule(
        petId: String,
        scheduleId: String,
        request: UpdateScheduleRequest
    ): Result<CreateScheduleRequest> = safeApiCall {
        client.put(
            ApiRoutes.UPDATE_SCHEDULE
                .replace("{id}", petId)
                .replace("{scheduleId}", scheduleId)
        ) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<CreateScheduleRequest>()
    }

    suspend fun deleteSchedule(petId: String, scheduleId: String): Result<Unit> = safeApiCall {
        client.delete(
            ApiRoutes.DELETE_SCHEDULE
                .replace("{id}", petId)
                .replace("{scheduleId}", scheduleId)
        )
    }

    // PET CARE
    suspend fun addPetCare(petId: String, request: CreatePetCareRequest): Result<CreatePetCareRequest> = safeApiCall {
        client.post(ApiRoutes.ADD_PET_CARE.replace("{id}", petId)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<CreatePetCareRequest>()
    }

    suspend fun updatePetCare(
        petId: String,
        careId: String,
        request: UpdatePetCareRequest
    ): Result<CreatePetCareRequest> = safeApiCall {
        client.put(
            ApiRoutes.UPDATE_PET_CARE
                .replace("{id}", petId)
                .replace("{careId}", careId)
        ) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<CreatePetCareRequest>()
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
    ): Result<CreateMedicalRecordRequest> = safeApiCall {
        client.post(ApiRoutes.ADD_MEDICAL_RECORD.replace("{id}", petId)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<CreateMedicalRecordRequest>()
    }

    suspend fun updateMedicalRecord(
        petId: String,
        recordId: String,
        request: UpdateMedicalRecordRequest
    ): Result<CreateMedicalRecordRequest> = safeApiCall {
        client.put(
            ApiRoutes.UPDATE_MEDICAL_RECORD
                .replace("{id}", petId)
                .replace("{recordId}", recordId)
        ) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<CreateMedicalRecordRequest>()
    }

    suspend fun deleteMedicalRecord(petId: String, recordId: String): Result<Unit> = safeApiCall {
        client.delete(
            ApiRoutes.DELETE_MEDICAL_RECORD
                .replace("{id}", petId)
                .replace("{recordId}", recordId)
        )
    }

    // PET IDS
    suspend fun addPetId(petId: String, request: CreatePetIdRequest): Result<CreatePetIdRequest> = safeApiCall {
        client.post(ApiRoutes.ADD_PET_ID.replace("{id}", petId)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<CreatePetIdRequest>()
    }

    suspend fun updatePetId(
        petId: String,
        petIdRecordId: String,
        request: UpdatePetIdRequest
    ): Result<CreatePetIdRequest> = safeApiCall {
        client.put(
            ApiRoutes.UPDATE_PET_ID
                .replace("{id}", petId)
                .replace("{petIdRecordId}", petIdRecordId)
        ) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<CreatePetIdRequest>()
    }

    suspend fun deletePetId(petId: String, petIdRecordId: String): Result<Unit> = safeApiCall {
        client.delete(
            ApiRoutes.DELETE_PET_ID
                .replace("{id}", petId)
                .replace("{petIdRecordId}", petIdRecordId)
        )
    }

    // API Response wrapper
    data class ApiResponse<T>(
        val success: Boolean,
        val data: T?,
        val message: String?,
        val errors: List<String>?
    )

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


//    // USERS
//    suspend fun fetchAllUsers(): Result<List<UserDTO>> = safeApiCall {
//        client.get(ApiRoutes.USERS).body<List<UserDTO>>()
//    }
//
//    suspend fun fetchUserById(id: String): Result<UserDTO> = safeApiCall {
//        client.get(ApiRoutes.USER_BY_ID.replace("{id}", id)).body<UserDTO>()
//    }
//
//    suspend fun fetchUserByEmail(email: String): Result<UserDTO> = safeApiCall {
//        client.get(ApiRoutes.USER_BY_EMAIL.replace("{email}", email)).body<UserDTO>()
//    }
//
//    suspend fun createUser(request: CreateUserRequest): Result<UserDTO> = safeApiCall {
//        client.post(ApiRoutes.USERS) {
//            contentType(ContentType.Application.Json)
//            setBody(request)
//        }.body<UserDTO>()
//    }
//
//    suspend fun updateUser(id: String, request: UpdateUserRequest): Result<UserDTO> = safeApiCall {
//        client.put(ApiRoutes.USER_BY_ID.replace("{id}", id)) {
//            contentType(ContentType.Application.Json)
//            setBody(request)
//        }.body<UserDTO>()
//    }
//
//    suspend fun updateUserRole(id: String, request: UpdateUserRoleRequest): Result<UserDTO> = safeApiCall {
//        client.put(ApiRoutes.UPDATE_USER_ROLE.replace("{id}", id)) {
//            contentType(ContentType.Application.Json)
//            setBody(request)
//        }.body<UserDTO>()
//    }
//
//    suspend fun deleteUser(id: String): Result<Unit> = safeApiCall {
//        client.delete(ApiRoutes.USER_BY_ID.replace("{id}", id))
//    }

    // Safe API call wrapper with proper error handling
}

package org.jikisan.taily.domain.pet

import io.github.aakira.napier.Napier
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import org.jikisan.cmpecommerceapp.util.ApiRoutes.TAG
import org.jikisan.taily.data.local.mockdata.MockData.MOCK_USERID
import org.jikisan.taily.data.mapper.toDomain
import org.jikisan.taily.domain.home.HomeRepository
import org.jikisan.taily.domain.mapper.toCreateRequest
import org.jikisan.taily.domain.mapper.toUpdateRequest
import org.jikisan.taily.domain.model.pet.DeletePet
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.domain.validator.isNetworkError
import org.jikisan.taily.util.NetworkExceptionHandler.handleNetworkException
import org.jikisan.taily.viewmodel.PetApiService
import kotlin.time.Duration.Companion.seconds

class PetRepositoryImpl(private val petApi: PetApiService) : PetRepository {

    override suspend fun createPet(pet: Pet): Result<Pet> {
        val request = pet.toCreateRequest()
        return petApi.createPet(request)
            .mapCatching { it.toDomain() }
    }

    override suspend fun updatePet(pet: Pet): Result<Pet> {
        val request = pet.toUpdateRequest()
        return petApi.updatePet(request)
            .mapCatching { it.toDomain() }
    }

    override suspend fun getPets(): Flow<List<Pet>> = flow {
        try {
            val result = petApi.fetchAllPets()
            if (result.isSuccess) {
                val pets = result.getOrThrow().map { it.toDomain() }
                Napier.v("$TAG Get All Pets")
                emit(pets)
            } else {
                val exception = result.exceptionOrNull()
                handleNetworkException(exception)
            }
        } catch (e: Exception) {
            handleNetworkException(e)
        }
    }
        .retry(retries = 2) { cause ->
            // Retry on generic exceptions (Kotlin Multiplatform compatible)
            Napier.v("$TAG Retry to Get All Pets")
            when (cause) {
                is ConnectTimeoutException -> {
                    Napier.w("$TAG Connection timeout, retrying...")
                    delay(2.seconds)
                    true
                }

                is SocketTimeoutException -> {
                    Napier.w("$TAG Socket timeout, retrying...")
                    delay(1.seconds)
                    true
                }

                is Exception -> {
                    // Check if the exception message indicates network issues (including iOS-specific messages)
                    val message = cause.message?.lowercase() ?: ""
                    if (
                        message.contains("network") ||
                        message.contains("connection") ||
                        message.contains("timeout") ||
                        message.contains("internet connection appears to be offline") ||
                        message.contains("unable to resolve host") ||
                        message.contains("no address associated with hostname")
                    ) {
                        Napier.w("$TAG Network-related error, retrying...")
                        delay(1.seconds)
                        true
                    } else {
                        false
                    }
                }

                else -> false
            }
        }
        .flowOn(Dispatchers.IO)

    override suspend fun  getPetsByUserId(): Flow<List<Pet>> = flow {
        try {
            val result = petApi.fetchPetByUserId(MOCK_USERID)
            if (result.isSuccess) {
                val pets = result.getOrThrow().map { it.toDomain() }
                Napier.v("$TAG Get All Pets By User ID")
                emit(pets)
            } else {
                val exception = result.exceptionOrNull()
                handleNetworkException(exception)
            }
        } catch (e: Exception) {
            handleNetworkException(e)
        }
    }
        .retry(retries = 2) { cause ->
            // Retry on generic exceptions (Kotlin Multiplatform compatible)
            Napier.v("$TAG Retry to Get All Pets")
            when (cause) {
                is ConnectTimeoutException -> {
                    Napier.w("$TAG Connection timeout, retrying...")
                    delay(2.seconds)
                    true
                }

                is SocketTimeoutException -> {
                    Napier.w("$TAG Socket timeout, retrying...")
                    delay(1.seconds)
                    true
                }

                is Exception -> {
                    // Check if the exception message indicates network issues (including iOS-specific messages)
                    val message = cause.message?.lowercase() ?: ""
                    if (
                        message.contains("network") ||
                        message.contains("connection") ||
                        message.contains("timeout") ||
                        message.contains("internet connection appears to be offline") ||
                        message.contains("unable to resolve host") ||
                        message.contains("no address associated with hostname")
                    ) {
                        Napier.w("$TAG Network-related error, retrying...")
                        delay(1.seconds)
                        true
                    } else {
                        false
                    }
                }

                else -> false
            }
        }
        .flowOn(Dispatchers.IO)

    override suspend fun getPetDetails(petId: String): Flow<Pet> = flow {

        try {
            val result = petApi.fetchPetById(petId)

            if (result.isSuccess) {
                val pet = result.getOrThrow().toDomain()
                Napier.v("$TAG load pet details")

                emit(pet)
            } else {
                val exception = result.exceptionOrNull()
                throw Exception("Error loading pet details: ${exception?.message}")
            }
        } catch (e: Exception) {
            Exception("Error loading pet details: ${e.message}")
        }
    }
        .retry(retries = 2) { cause ->
            // Retry on generic exceptions (Kotlin Multiplatform compatible)
            Napier.v("$TAG Retry to load pet details")
            when (cause) {
                is ConnectTimeoutException -> {
                    Napier.w("$TAG Connection timeout, retrying...")
                    delay(2.seconds)
                    true
                }

                is SocketTimeoutException -> {
                    Napier.w("$TAG Socket timeout, retrying...")
                    delay(1.seconds)
                    true
                }

                is Exception -> {
                    // Check if the exception message indicates network issues (including iOS-specific messages)
                    val message = cause.message?.lowercase() ?: ""
                    if (isNetworkError(message) ) {
                        Napier.w("$TAG Network-related error, retrying...")
                        delay(1.seconds)
                        true
                    } else {
                        false
                    }
                }

                else -> false
            }
        }
        .flowOn(Dispatchers.IO)

    override suspend fun deletePet(petId: String): Flow<DeletePet> = flow {

        try {
            val result = petApi.deletePet(petId)
            if (result.isSuccess) {
                val deletedPetResult = result.getOrThrow().toDomain()
                Napier.i("$TAG REPO delete pet")
                emit(deletedPetResult)
            } else {
                val exception = result.exceptionOrNull()
                Napier.e("$TAG REPO Error deleting pet: ${exception?.message}")
                throw Exception("$TAG Error deleting pet")
            }
        } catch (e: Exception) {
            Napier.e("$TAG REPO Error deleting pet: ${e.message}")
            Exception("$TAG Error deleting pet")
            throw e
        }
    }
}
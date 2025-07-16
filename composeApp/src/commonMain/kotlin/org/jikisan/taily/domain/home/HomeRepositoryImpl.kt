package org.jikisan.taily.domain.home

import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import org.jikisan.cmpecommerceapp.util.ApiRoutes.TAG
import org.jikisan.taily.data.mapper.toDomain
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.viewmodel.PetApiService
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import org.jikisan.taily.data.local.mockdata.MockData.MOCK_USERID
import org.jikisan.taily.domain.validator.isNetworkError
import org.jikisan.taily.util.NetworkExceptionHandler.handleNetworkException
import kotlin.time.Duration.Companion.seconds

class HomeRepositoryImpl(private val petApi: PetApiService) : HomeRepository {


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


    override suspend fun getPetsByUserId(): Flow<List<Pet>> = flow {
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


}
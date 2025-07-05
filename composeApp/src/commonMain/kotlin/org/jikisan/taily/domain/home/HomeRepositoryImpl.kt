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
import org.jikisan.taily.model.pet.PetDTO
import org.jikisan.taily.viewmodel.PetApiService
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
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

    private fun handleNetworkException(exception: Throwable?) {
        when (exception) {
            is ConnectTimeoutException -> {
                Napier.e("$TAG No internet connection. Please check your network.")
                throw exception
            }
            is SocketTimeoutException -> {
                Napier.e("$TAG Request timed out. Please try again.")
                throw exception
            }
            else -> {
                val message = exception?.message?.lowercase() ?: ""
                when {
                    message.contains("unable to resolve host", true) ||
                            message.contains("no address associated with hostname", true) ||
                            message.contains("internet connection appears to be offline", true) ||
                            message.contains("network is unreachable", true) -> {
                        val error = "No internet. Check network."
                        Napier.e("$TAG $error")
                        throw Exception(error)
                    }
                    message.contains("network", true) ||
                            message.contains("connection", true) -> {
                        val error = "Network error. Check connection."
                        Napier.e("$TAG $error")
                        throw exception ?: Exception(error)
                    }
                    message.contains("timeout", true) -> {
                        val error = "Request timed out. Try again."
                        Napier.e("$TAG $error")
                        throw exception ?: Exception(error)
                    }
                    else -> {
                        val error = "Error fetching pets"
                        Napier.e("$TAG $error")
                        throw exception ?: Exception(error)
                    }
                }

            }
        }
        Napier.e("$TAG Get All Pets Failed")
    }
}
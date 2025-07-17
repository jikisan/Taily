package org.jikisan.taily.domain.petdetails

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
import org.jikisan.taily.data.mapper.toDomain
import org.jikisan.taily.domain.model.pet.DeletePet
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.domain.validator.isNetworkError
import org.jikisan.taily.viewmodel.PetApiService
import kotlin.time.Duration.Companion.seconds

class PetDetailsRepositoryImpl(private val service: PetApiService) : PetDetailsRepository {

    override suspend fun loadPetDetails(petId: String): Flow<Pet> = flow {

        try {
            val result = service.fetchPetById(petId)

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
            val result = service.deletePet(petId)
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
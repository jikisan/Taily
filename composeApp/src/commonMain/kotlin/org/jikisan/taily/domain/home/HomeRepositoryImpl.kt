package org.jikisan.taily.domain.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import org.jikisan.taily.data.toDomain
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.viewmodel.PetApi
import kotlin.time.Duration.Companion.seconds

class HomeRepositoryImpl(private val petApi: PetApi) : HomeRepository {

    override suspend fun getPets(): Flow<List<Pet>> = flow {
        try {
            val pets = petApi.fetchAllPets().map { it.toDomain() }
            emit(pets)
        } catch (e: Exception) {
            // Log the error for debugging purposes
            println("Error fetching pets: ${e.message}")

            // Emit empty list as fallback
            emit(emptyList<Pet>())
        }
    }
        .retry(retries = 2) { cause ->
            // Retry on generic exceptions (Kotlin Multiplatform compatible)
            when (cause) {
                is Exception -> {
                    delay(1.seconds)
                    true
                }
                else -> false
            }
        }
        .flowOn(Dispatchers.IO)
}
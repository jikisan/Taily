package org.jikisan.taily.domain.pet

import io.github.aakira.napier.Napier
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
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.viewmodel.PetApiService
import kotlin.time.Duration.Companion.seconds

class PetRepositoryImpl(private val petApi: PetApiService) : PetRepository {


    override suspend fun getPets(): Flow<List<Pet>> = flow {
        val result = petApi.fetchAllPets()
        if (result.isSuccess) {
            val pets = result.getOrThrow().map { it.toDomain() }
            Napier.v("$TAG Get All Pets")
            emit(pets)
        } else {
            Napier.e("$TAG Error fetching pets: ${result.exceptionOrNull()?.message}")
            Napier.e("$TAG Get All Pets Failed")
            emit(emptyList()) // fallback for UI
        }
    }
        .retry(retries = 2) { cause ->
            // Retry on generic exceptions (Kotlin Multiplatform compatible)
            Napier.v("$TAG Retry to Get All Pets")
            when (cause) {
                is Exception -> {
                    delay(1.seconds)
                    true
                }
                else -> false
            }
        }
        .flowOn(Dispatchers.IO)

    override suspend fun getPetsByUserId(): Flow<List<Pet>> = flow {

        val result = petApi.fetchPetByUserId(MOCK_USERID)

        if (result.isSuccess) {
            val pets = result.getOrThrow().map { it.toDomain() }
            Napier.v("$TAG Get All Pets")
            emit(pets)
        } else {
            Napier.e("$TAG Error fetching pets: ${result.exceptionOrNull()?.message}")
            Napier.e("$TAG Get All Pets Failed")
            emit(emptyList()) // fallback for UI
        }
    }
        .retry(retries = 2) { cause ->
            // Retry on generic exceptions (Kotlin Multiplatform compatible)
            Napier.v("$TAG Retry to Get All Pets")
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
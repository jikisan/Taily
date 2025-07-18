package org.jikisan.taily.domain.pet

import kotlinx.coroutines.flow.Flow
import org.jikisan.taily.domain.model.pet.DeletePet
import org.jikisan.taily.domain.model.pet.Pet

interface PetRepository {

    suspend fun createPet(pet: Pet): Result<Pet>
    suspend fun getPets(): Flow<List<Pet>>
    suspend fun getPetsByUserId(): Flow<List<Pet>>

    suspend fun getPetDetails(petId: String): Flow<Pet>

    suspend fun deletePet(petId: String): Flow<DeletePet>
}
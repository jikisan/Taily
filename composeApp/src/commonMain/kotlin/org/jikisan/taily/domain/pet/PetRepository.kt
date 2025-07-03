package org.jikisan.taily.domain.pet

import kotlinx.coroutines.flow.Flow
import org.jikisan.taily.domain.model.pet.Pet

interface PetRepository {
    suspend fun getPets(): Flow<List<Pet>>
}
package org.jikisan.taily.domain.home

import kotlinx.coroutines.flow.Flow
import org.jikisan.taily.domain.model.pet.Pet

interface HomeRepository {

    suspend fun getPets(): Flow<List<Pet>>
    suspend fun getPetsByUserId(): Flow<List<Pet>>
}
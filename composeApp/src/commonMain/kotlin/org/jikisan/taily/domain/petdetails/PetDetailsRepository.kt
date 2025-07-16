package org.jikisan.taily.domain.petdetails

import kotlinx.coroutines.flow.Flow
import org.jikisan.taily.domain.model.pet.Pet

interface PetDetailsRepository {

    suspend fun loadPetDetails(petId: String): Flow<Pet>
}
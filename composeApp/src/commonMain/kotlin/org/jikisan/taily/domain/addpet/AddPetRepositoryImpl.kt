package org.jikisan.taily.domain.addpet

import org.jikisan.taily.data.mapper.toDomain
import org.jikisan.taily.domain.mapper.toCreateRequest
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.viewmodel.PetApiService

class AddPetRepositoryImpl(private val apiService: PetApiService) : AddPetRepository {

    override suspend fun createPet(pet: Pet): Result<Pet> {
        val request = pet.toCreateRequest()
        return apiService.createPet(request)
            .mapCatching { it.toDomain() }
    }
}
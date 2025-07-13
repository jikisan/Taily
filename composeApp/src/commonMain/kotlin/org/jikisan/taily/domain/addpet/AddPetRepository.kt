package org.jikisan.taily.domain.addpet

import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.PetDTO

interface AddPetRepository {

    suspend fun createPet(pet: Pet): Result<Pet>
}
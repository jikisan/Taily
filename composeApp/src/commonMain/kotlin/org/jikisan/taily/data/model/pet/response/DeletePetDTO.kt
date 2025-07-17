package org.jikisan.taily.data.model.pet.response

import kotlinx.serialization.Serializable
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.PetDTO

@Serializable
data class DeletePetDTO(
    val message: String,
    val pet: PetDTO
)
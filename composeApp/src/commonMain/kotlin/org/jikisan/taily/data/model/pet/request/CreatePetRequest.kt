package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO
import org.jikisan.taily.data.model.pet.base.PetBase

@Serializable
data class CreatePetRequest(
    override val name: String,
    override val petType: String,
    override val breed: String,
    override val dateOfBirth: String,
    override val gender: String,
    override val photoUrl: String,
    override val weight: WeightDTO,
    override val ownerId: OwnerDTO,
    override val identifiers: IdentifiersDTO
) : PetBase
package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO
import org.jikisan.taily.data.model.pet.base.PetBase
import org.jikisan.taily.data.model.pet.response.PhotoDTO

@Serializable
data class CreatePetRequest(
    override val name: String,
    override val petType: String,
    override val breed: String,
    override val dateOfBirth: String,
    override val gender: String,
    override val photo: PhotoDTO,
    override val weight: WeightDTO,
    override val ownerId: OwnerDTO,
    override val identifiers: IdentifiersDTO
) : PetBase
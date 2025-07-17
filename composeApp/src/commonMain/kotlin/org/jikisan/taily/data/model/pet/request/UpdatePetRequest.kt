package org.jikisan.taily.data.model.pet.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO
import org.jikisan.taily.model.pet.IdentifiersDTO
import org.jikisan.taily.model.pet.OwnerDTO

@Serializable
data class UpdatePetRequest(
    @SerialName("_id")
    val id: String,
    val name: String,
    val petType: String,
    val breed: String,
    val dateOfBirth: String,
    val gender: String,
    val photoUrl: String,
    val weight: WeightDTO,
    val ownerId: OwnerDTO,
    val identifiers: IdentifiersDTO,
)
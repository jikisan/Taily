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
    @SerialName("name")
    val name: String,
    @SerialName("petType")
    val petType: String,
    @SerialName("breed")
    val breed: String,
    @SerialName("dateOfBirth")
    val dateOfBirth: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("photoUrl")
    val photoUrl: String,
    @SerialName("weight")
    val weight: WeightDTO,
    @SerialName("ownerId")
    val ownerId: OwnerDTO,
    @SerialName("identifiers")
    val identifiers: IdentifiersDTO,
)
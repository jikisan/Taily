package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PetIdDTO(
    @SerialName("_id")
    val id: String,
    val idName: String,
    val idUrl: String,
    val petId: String
)
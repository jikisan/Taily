package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePetIdRequest(
    @SerialName("idName")
    val idName: String,
    @SerialName("idUrl")
    val idUrl: String,
    @SerialName("petId")
    val petId: String
)
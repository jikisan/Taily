package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePetIdRequest(
    val idName: String,
    val idUrl: String,
    val petId: String
)
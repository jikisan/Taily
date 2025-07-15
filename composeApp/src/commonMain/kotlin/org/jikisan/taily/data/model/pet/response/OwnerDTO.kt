package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OwnerDTO(
    val email: String,
    val fullName: String,
    @SerialName("_id")
    val id: String,
    @SerialName("id")
    val userId: String
)
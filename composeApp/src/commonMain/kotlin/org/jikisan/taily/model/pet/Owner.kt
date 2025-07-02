package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    @SerialName("email")
    val email: String,
    @SerialName("fullName")
    val fullName: String,
    @SerialName("_id")
    val id: String,
    @SerialName("id")
    val userId: String
)
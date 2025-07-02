package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GivenDTO(
    @SerialName("isGiven")
    val isGiven: Boolean
)
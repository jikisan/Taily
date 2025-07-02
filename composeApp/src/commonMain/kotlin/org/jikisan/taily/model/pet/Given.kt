package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Given(
    @SerialName("isGiven")
    val isGiven: Boolean
)
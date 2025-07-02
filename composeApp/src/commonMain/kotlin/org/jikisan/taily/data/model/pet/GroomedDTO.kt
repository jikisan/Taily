package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroomedDTO(
    @SerialName("groomedDateTime")
    val groomedDateTime: String,
    @SerialName("isGroomed")
    val isGroomed: Boolean,
    @SerialName("referencePhoto")
    val referencePhoto: String
)
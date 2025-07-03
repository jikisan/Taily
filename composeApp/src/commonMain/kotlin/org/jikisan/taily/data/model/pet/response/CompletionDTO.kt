package org.jikisan.taily.data.model.pet.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompletionDTO(
    @SerialName("dateTime")
    val dateTime: String,
    @SerialName("isComplete")
    val isComplete: Boolean,
    @SerialName("referencePhoto")
    val referencePhoto: String
)
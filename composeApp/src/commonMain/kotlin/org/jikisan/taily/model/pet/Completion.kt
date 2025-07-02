package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Completion(
    @SerialName("dateTime")
    val dateTime: String,
    @SerialName("isComplete")
    val isComplete: Boolean,
    @SerialName("referencePhoto")
    val referencePhoto: String
)
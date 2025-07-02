package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Identifiers(
    @SerialName("allergies")
    val allergies: List<String>,
    @SerialName("clipLocation")
    val clipLocation: String,
    @SerialName("colorMarkings")
    val colorMarkings: String,
    @SerialName("isNeuteredOrSpayed")
    val isNeuteredOrSpayed: Boolean,
    @SerialName("microchipLocation")
    val microchipLocation: String,
    @SerialName("microchipNumber")
    val microchipNumber: String,
    @SerialName("size")
    val size: String
)
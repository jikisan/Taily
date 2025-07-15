package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable

@Serializable
data class IdentifiersDTO(
    val allergies: List<String> = emptyList(),
    val clipLocation: String? = null,
    val colorMarkings: String? = null,
    val isNeuteredOrSpayed: Boolean? = null,
    val microchipLocation: String? = null,
    val microchipNumber: String? = null,
    val size: String? = null
)
package org.jikisan.taily.model.pet

data class Identifiers(
    val allergies: List<String> = emptyList(),
    val clipLocation: String? = null,
    val colorMarkings: String? = null,
    val isNeuteredOrSpayed: Boolean? = null,
    val microchipLocation: String? = null,
    val microchipNumber: String? = null,
    val size: String? = null
)
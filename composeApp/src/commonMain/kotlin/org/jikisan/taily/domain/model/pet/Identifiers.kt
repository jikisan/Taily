package org.jikisan.taily.model.pet

data class Identifiers(
    val allergies: List<String>,
    val clipLocation: String,
    val colorMarkings: String,
    val isNeuteredOrSpayed: Boolean,
    val microchipLocation: String,
    val microchipNumber: String,
    val size: String
)
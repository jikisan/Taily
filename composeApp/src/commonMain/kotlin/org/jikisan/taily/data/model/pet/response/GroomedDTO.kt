package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable

@Serializable
data class GroomedDTO(
    val groomedDateTime: String? = null,
    val isGroomed: Boolean,
    val referencePhoto: String? = null
)
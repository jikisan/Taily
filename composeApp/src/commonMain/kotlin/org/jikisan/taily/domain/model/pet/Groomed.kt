package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable

data class Groomed(
    val groomedDateTime: String? = null,
    val isGroomed: Boolean,
    val referencePhoto: String? = null,
)
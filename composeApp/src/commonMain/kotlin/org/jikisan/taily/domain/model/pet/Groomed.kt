package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable
import org.jikisan.taily.domain.model.Photo

data class Groomed(
    val groomedDateTime: String? = null,
    val isGroomed: Boolean,
    val referencePhoto: Photo? = null,
)
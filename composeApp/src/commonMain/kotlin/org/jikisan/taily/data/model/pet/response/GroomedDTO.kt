package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.pet.response.PhotoDTO

@Serializable
data class GroomedDTO(
    val groomedDateTime: String? = null,
    val isGroomed: Boolean,
    val referencePhoto: PhotoDTO? = null
)
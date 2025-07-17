package org.jikisan.taily.data.model.pet.response

import kotlinx.serialization.Serializable

@Serializable
data class CompletionDTO(
    val dateTime: String? = null,
    val isComplete: Boolean,
    val referencePhoto: String? = null
)
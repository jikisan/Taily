package org.jikisan.taily.domain.model.pet

import kotlinx.serialization.Serializable

data class Completion(
    val dateTime: String,
    val isComplete: Boolean,
    val referencePhoto: String
)
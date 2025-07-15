package org.jikisan.taily.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeightDTO(
    val unit: String,
    val value: Double
)
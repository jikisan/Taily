package org.jikisan.taily.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeightDTO(
    @SerialName("unit")
    val unit: String,
    @SerialName("value")
    val value: Double
)
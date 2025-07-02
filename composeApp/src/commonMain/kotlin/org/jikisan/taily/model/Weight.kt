package org.jikisan.taily.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weight(
    @SerialName("unit")
    val unit: String,
    @SerialName("value")
    val value: Double
)
package org.jikisan.taily.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Weight(
    val unit: String,
    val value: Double
)
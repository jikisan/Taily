package org.jikisan.taily.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Weight(
    val value: Double,
    val unit: String
)
package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable
import org.jikisan.taily.domain.model.Photo

data class Given(
    val isGiven: Boolean? = false,
    val type: String? = "",
    val dateTime: String? = "",
    val proofPhoto: Photo? = null
)

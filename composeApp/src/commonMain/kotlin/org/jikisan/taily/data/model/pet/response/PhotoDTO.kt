package org.jikisan.taily.data.model.pet.response

import kotlinx.serialization.Serializable

@Serializable
data class PhotoDTO(
    val name: String,
    val url: String
)

package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.pet.response.PhotoDTO

@Serializable
data class GivenDTO(
    val isGiven: Boolean? = false,
    val type: String? = "",
    val dateTime: String? = "",
    val proofPhoto: PhotoDTO? = null
)
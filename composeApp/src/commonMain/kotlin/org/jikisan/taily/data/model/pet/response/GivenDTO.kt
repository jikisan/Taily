package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable

@Serializable
data class GivenDTO(
    val isGiven: Boolean? = false,
    val type: String? = "",
    val dateTime: String? = "",
    val proofPhoto: String? = ""
)
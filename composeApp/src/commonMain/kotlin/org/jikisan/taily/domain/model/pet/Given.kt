package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable

data class Given(
    val isGiven: Boolean? = false,
    val type: String? = "",
    val dateTime: String? = "",
    val proofPhoto: String? = ""
)

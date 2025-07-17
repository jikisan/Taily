package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable

data class Owner(
    val email: String,
    val fullName: String,
    val id: String,
    val userId: String
)
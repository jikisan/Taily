package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable

data class PetId(
    val id: String,
    val idName: String,
    val idUrl: String,
    val petId: String
)
package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO

@Serializable
data class CreatePetCareRequest(
    val careType: String,
    val clinic: String,
    val groomingDateTime: String,
    val id: String,
    val notes: String,
)
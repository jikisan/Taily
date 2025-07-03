package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO

@Serializable
data class CreatePetCareRequest(
    @SerialName("careType")
    val careType: String,
    @SerialName("clinic")
    val clinic: String,
    @SerialName("groomingDateTime")
    val groomingDateTime: String,
    @SerialName("_id")
    val id: String,
    @SerialName("notes")
    val notes: String,
)
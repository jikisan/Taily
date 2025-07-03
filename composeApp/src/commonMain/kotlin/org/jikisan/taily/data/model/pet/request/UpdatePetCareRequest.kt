package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO

@Serializable
data class UpdatePetCareRequest(
    @SerialName("groomed")
    val groomed: GroomedDTO,
    @SerialName("groomer")
    val groomer: String,
    @SerialName("notes")
    val notes: String,
    @SerialName("weight")
    val weight: WeightDTO
)
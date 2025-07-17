package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO

@Serializable
data class PetCareDTO(
    val careType: String,
    val clinic: String,
    val groomed: GroomedDTO,
    val groomer: String,
    val groomingDateTime: String,
    @SerialName("_id")
    val id: String,
    val notes: String,
    val weight: WeightDTO
)
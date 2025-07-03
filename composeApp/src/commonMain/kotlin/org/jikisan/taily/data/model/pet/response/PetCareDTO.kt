package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO

@Serializable
data class PetCareDTO(
    @SerialName("careType")
    val careType: String,
    @SerialName("clinic")
    val clinic: String,
    @SerialName("groomed")
    val groomed: GroomedDTO,
    @SerialName("groomer")
    val groomer: String,
    @SerialName("groomingDateTime")
    val groomingDateTime: String,
    @SerialName("_id")
    val id: String,
    @SerialName("notes")
    val notes: String,
    @SerialName("weight")
    val weight: WeightDTO
)
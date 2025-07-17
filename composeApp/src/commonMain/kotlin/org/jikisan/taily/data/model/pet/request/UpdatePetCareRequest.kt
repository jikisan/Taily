package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO

@Serializable
data class UpdatePetCareRequest(
    val groomed: GroomedDTO,
    val groomer: String,
    val notes: String,
    val weight: WeightDTO
)
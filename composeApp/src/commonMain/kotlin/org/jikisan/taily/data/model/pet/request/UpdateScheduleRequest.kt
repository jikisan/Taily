package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO

@Serializable
data class UpdateScheduleRequest(
    @SerialName("given")
    val given: GivenDTO,
    @SerialName("notes")
    val notes: String,
    @SerialName("vaccineType")
    val vaccineType: String,
    @SerialName("vet")
    val vet: String,
    @SerialName("weight")
    val weight: WeightDTO
)
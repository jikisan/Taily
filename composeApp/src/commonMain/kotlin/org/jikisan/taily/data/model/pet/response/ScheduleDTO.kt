package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO

@Serializable
data class ScheduleDTO(
    val given: GivenDTO,
    val hospital: String,
    @SerialName("_id")
    val id: String,
    val notes: String,
    val schedDateTime: String,
    val vaccineType: String,
    val vet: String,
    val weight: WeightDTO
)
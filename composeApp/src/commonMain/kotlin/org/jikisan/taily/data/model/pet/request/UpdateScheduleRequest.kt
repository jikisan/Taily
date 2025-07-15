package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO

@Serializable
data class UpdateScheduleRequest(
    val given: GivenDTO,
    val notes: String,
    val vaccineType: String,
    val vet: String,
    val weight: WeightDTO
)
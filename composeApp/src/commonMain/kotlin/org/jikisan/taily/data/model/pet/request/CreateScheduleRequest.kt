package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO

@Serializable
data class CreateScheduleRequest(
    val hospital: String,
    val notes: String,
    val schedDateTime: String,
    val vaccineType: String,
    val vet: String
)
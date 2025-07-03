package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO

@Serializable
data class CreateScheduleRequest(
    @SerialName("hospital")
    val hospital: String,
    @SerialName("notes")
    val notes: String,
    @SerialName("schedDateTime")
    val schedDateTime: String,
    @SerialName("vaccineType")
    val vaccineType: String,
    @SerialName("vet")
    val vet: String
)
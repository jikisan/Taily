package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO
import org.jikisan.taily.data.model.pet.response.CompletionDTO

@Serializable
data class CreateMedicalRecordRequest(
    @SerialName("clinic")
    val clinic: String,
    @SerialName("medicalDateTime")
    val medicalDateTime: String,
    @SerialName("medicalType")
    val medicalType: String,
    @SerialName("notes")
    val notes: String,
    @SerialName("symptoms")
    val symptoms: String,
)
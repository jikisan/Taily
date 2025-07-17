package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO
import org.jikisan.taily.data.model.pet.response.CompletionDTO

@Serializable
data class CreateMedicalRecordRequest(
    val clinic: String,
    val medicalDateTime: String,
    val medicalType: String,
    val notes: String,
    val symptoms: String,
)
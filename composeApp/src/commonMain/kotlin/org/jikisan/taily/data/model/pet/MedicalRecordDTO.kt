package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.pet.CompletionDTO
import org.jikisan.taily.model.Weight

@Serializable
data class MedicalRecordDTO(
    @SerialName("clinic")
    val clinic: String,
    @SerialName("completion")
    val completion: CompletionDTO,
    @SerialName("diagnosis")
    val diagnosis: String,
    @SerialName("_id")
    val id: String,
    @SerialName("medicalDateTime")
    val medicalDateTime: String,
    @SerialName("medicalType")
    val medicalType: String,
    @SerialName("notes")
    val notes: String,
    @SerialName("prescription")
    val prescription: String,
    @SerialName("symptoms")
    val symptoms: String,
    @SerialName("vet")
    val vet: String,
    @SerialName("weight")
    val weight: Weight
)
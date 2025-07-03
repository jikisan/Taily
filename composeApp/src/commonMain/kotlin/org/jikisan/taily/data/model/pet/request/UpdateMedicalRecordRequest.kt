package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO
import org.jikisan.taily.data.model.pet.response.CompletionDTO

@Serializable
data class UpdateMedicalRecordRequest(
    @SerialName("completion")
    val completion: CompletionDTO,
    @SerialName("diagnosis")
    val diagnosis: String,
    @SerialName("notes")
    val notes: String,
    @SerialName("prescription")
    val prescription: String,
    @SerialName("symptoms")
    val symptoms: String,
    @SerialName("vet")
    val vet: String,
    @SerialName("weight")
    val weight: WeightDTO
)
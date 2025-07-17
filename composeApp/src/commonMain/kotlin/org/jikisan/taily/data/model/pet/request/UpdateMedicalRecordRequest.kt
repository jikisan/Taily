package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO
import org.jikisan.taily.data.model.pet.response.CompletionDTO

@Serializable
data class UpdateMedicalRecordRequest(
    val completion: CompletionDTO,
    val diagnosis: String,
    val notes: String,
    val prescription: String,
    val symptoms: String,
    val vet: String,
    val weight: WeightDTO
)
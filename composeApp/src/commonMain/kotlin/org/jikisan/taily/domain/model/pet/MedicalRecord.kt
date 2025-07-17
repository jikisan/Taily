package org.jikisan.taily.model.pet


import org.jikisan.taily.data.model.pet.response.CompletionDTO
import org.jikisan.taily.domain.model.Weight

data class MedicalRecord(
    val clinic: String,
    val completion: CompletionDTO,
    val diagnosis: String,
    val id: String,
    val medicalDateTime: String,
    val medicalType: String,
    val notes: String,
    val prescription: String,
    val symptoms: String,
    val vet: String,
    val weight: Weight
)
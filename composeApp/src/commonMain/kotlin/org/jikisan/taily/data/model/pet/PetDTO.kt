package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO

@Serializable
data class PetDTO(
    @SerialName("_id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("petType")
    val petType: String,
    @SerialName("breed")
    val breed: String,
    @SerialName("dateOfBirth")
    val dateOfBirth: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("photoUrl")
    val photoUrl: String,
    @SerialName("weight")
    val weight: WeightDTO,
    @SerialName("ownerId")
    val ownerId: OwnerDTO,
    @SerialName("identifiers")
    val identifiers: IdentifiersDTO,
    @SerialName("passport")
    val passport: PassportDTO,
    @SerialName("medicalRecords")
    val medicalRecords: List<MedicalRecordDTO>,
    @SerialName("petCare")
    val petCare: List<PetCareDTO>,
    @SerialName("petIds")
    val petIds: List<PetIdDTO>,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("__v")
    val v: Int
)
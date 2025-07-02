package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.model.Weight

@Serializable
data class PetItem(
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
    val weight: Weight,
    @SerialName("ownerId")
    val ownerId: Owner,
    @SerialName("identifiers")
    val identifiers: Identifiers,
    @SerialName("passport")
    val passport: Passport,
    @SerialName("medicalRecords")
    val medicalRecords: List<MedicalRecord>,
    @SerialName("petCare")
    val petCare: List<PetCare>,
    @SerialName("petIds")
    val petIds: List<PetId>,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("__v")
    val v: Int
)
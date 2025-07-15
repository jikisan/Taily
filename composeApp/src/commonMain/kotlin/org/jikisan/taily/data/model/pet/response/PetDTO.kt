package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.data.model.WeightDTO
import org.jikisan.taily.data.model.pet.base.PetBase

@Serializable
data class PetDTO(
    @SerialName("_id")
    val id: String,

    override val name: String,
    override val petType: String,
    override val breed: String,
    override val dateOfBirth: String,
    override val gender: String,
    override val photoUrl: String,
    override val weight: WeightDTO,
    override val ownerId: OwnerDTO,
    override val identifiers: IdentifiersDTO,

    val passport: PassportDTO,
    val medicalRecords: List<MedicalRecordDTO>,
    val petCare: List<PetCareDTO>,
    val petIds: List<PetIdDTO>,
    val createdAt: String,
    val updatedAt: String,
    @SerialName("__v")
    val v: Int,

) : PetBase
package org.jikisan.taily.domain.model.pet

import org.jikisan.taily.model.pet.MedicalRecord
import org.jikisan.taily.model.pet.Owner
import org.jikisan.taily.model.pet.Passport
import org.jikisan.taily.model.pet.PetCare
import org.jikisan.taily.model.pet.PetId
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.model.pet.Identifiers

data class Pet(
    val id: String,
    val name: String,
    val petType: String,
    val breed: String,
    val dateOfBirth: String,
    val gender: String,
    val photoUrl: String,
    val weight: Weight,
    val ownerId: Owner,
    val identifiers: Identifiers,
    val passport: Passport,
    val medicalRecords: List<MedicalRecord>,
    val petCare: List<PetCare>,
    val petIds: List<PetId>,
    val createdAt: String,
    val updatedAt: String,
    val v: Int
)

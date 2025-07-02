package org.jikisan.taily.data

import Identifiers
import org.jikisan.taily.data.model.WeightDTO
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.IdentifiersDTO
import org.jikisan.taily.model.pet.MedicalRecord
import org.jikisan.taily.model.pet.MedicalRecordDTO
import org.jikisan.taily.model.pet.Owner
import org.jikisan.taily.model.pet.OwnerDTO
import org.jikisan.taily.model.pet.Passport
import org.jikisan.taily.model.pet.PassportDTO
import org.jikisan.taily.model.pet.PetCare
import org.jikisan.taily.model.pet.PetCareDTO
import org.jikisan.taily.model.pet.PetDTO
import org.jikisan.taily.model.pet.PetId
import org.jikisan.taily.model.pet.PetIdDTO
import org.jikisan.taily.model.pet.Schedule
import org.jikisan.taily.model.pet.ScheduleDTO


fun PetDTO.toDomain(): Pet {
    return Pet(
        id = id,
        name = name,
        petType = petType,
        breed = breed,
        dateOfBirth = dateOfBirth,
        gender = gender,
        photoUrl = photoUrl,
        weight = weight.toDomain(),
        ownerId = ownerId.toDomain(),
        identifiers = identifiers.toDomain(),
        passport = passport.toDomain(),
        medicalRecords = medicalRecords.map { it.toDomain() },
        petCare = petCare.map { it.toDomain() },
        petIds = petIds.map { it.toDomain() },
        createdAt = createdAt,
        updatedAt = updatedAt,
        v = v
    )
}

fun MedicalRecordDTO.toDomain(): MedicalRecord {
    return MedicalRecord(
        clinic = clinic,
        completion = completion,
        diagnosis = diagnosis,
        id = id,
        medicalDateTime = medicalDateTime,
        medicalType = medicalType,
        notes = notes,
        prescription = prescription,
        symptoms = symptoms,
        vet = vet,
        weight = weight
    )
}

fun PetCareDTO.toDomain(): PetCare {
    return PetCare(
        careType = careType,
        clinic = clinic,
        groomed = groomed,
        groomer = groomer,
        groomingDateTime = groomingDateTime,
        id = id,
        notes = notes,
        weight = weight
    )
}

fun PetIdDTO.toDomain(): PetId {
    return PetId(
        id = id,
        idName = idName,
        idUrl = idUrl,
        petId = petId
    )
}

fun WeightDTO.toDomain(): Weight {
    return Weight(
        value = value,
        unit = unit
    )
}

fun OwnerDTO.toDomain(): Owner {
    return Owner(
        email = email,
        fullName = fullName,
        id = id,
        userId = userId
    )
}

fun IdentifiersDTO.toDomain(): Identifiers {
    return Identifiers(
        allergies = allergies,
        clipLocation = clipLocation,
        colorMarkings = colorMarkings,
        isNeuteredOrSpayed = isNeuteredOrSpayed,
        microchipLocation = microchipLocation,
        microchipNumber = microchipNumber,
        size = size
    )
}

fun PassportDTO.toDomain(): Passport {
    return Passport(
        schedules = schedules.map { it.toDomain() }
    )
}

fun ScheduleDTO.toDomain(): Schedule {
    return Schedule(
        given = given,
        hospital = hospital,
        id = id,
        notes = notes,
        schedDateTime = schedDateTime,
        vaccineType = vaccineType,
        vet = vet,
        weight = weight
    )
}
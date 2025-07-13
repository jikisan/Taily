package org.jikisan.taily.domain.mapper

import org.jikisan.taily.data.model.WeightDTO
import org.jikisan.taily.domain.model.Reminder
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.CreatePetRequest
import org.jikisan.taily.model.pet.Identifiers
import org.jikisan.taily.model.pet.IdentifiersDTO
import org.jikisan.taily.model.pet.Owner
import org.jikisan.taily.model.pet.OwnerDTO

fun Pet.toCreateRequest(): CreatePetRequest {
    return CreatePetRequest(
        name = name,
        petType = petType,
        breed = breed,
        dateOfBirth = dateOfBirth, // Format as ISO string if needed
        gender = gender,
        photoUrl = photoUrl,
        weight = weight.toDto(),
        ownerId = ownerId.toDto(),
        identifiers = identifiers.toDto()
    )
}

fun Weight.toDto() = WeightDTO(
    unit = unit,
    value = value
)
fun Owner.toDto() = OwnerDTO(
    email = email,
    fullName = fullName,
    id = id,
    userId = userId
)
fun Identifiers.toDto() = IdentifiersDTO(
    allergies = allergies,
    clipLocation = clipLocation,
    colorMarkings = colorMarkings,
    isNeuteredOrSpayed = isNeuteredOrSpayed,
    microchipLocation = microchipLocation,
    microchipNumber = microchipNumber,
    size = size
)

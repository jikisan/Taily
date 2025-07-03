package org.jikisan.taily.data.local.mockdata

import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.Identifiers
import org.jikisan.taily.model.pet.Owner
import org.jikisan.taily.model.pet.Passport

object MockData {
    val mockPets = listOf(
        Pet(
            id = "1",
            name = "Buddy",
            petType = "Dog",
            breed = "Golden Retriever",
            dateOfBirth = "2020-05-15",
            gender = "Male",
            photoUrl = "",
            weight = Weight(unit = "kg", value = 25.5),
            ownerId = Owner(
                id = "owner1",
                userId = "user1",
                email = "owner@example.com",
                fullName = "John Doe"
            ),
            identifiers = Identifiers(
                allergies = emptyList(),
                clipLocation = "Left ear",
                colorMarkings = "Golden coat with white chest",
                isNeuteredOrSpayed = true,
                microchipLocation = "Between shoulder blades",
                microchipNumber = "123456789",
                size = "Large"
            ),
            passport = Passport(schedules = emptyList()),
            medicalRecords = emptyList(),
            petCare = emptyList(),
            petIds = emptyList(),
            createdAt = "2024-01-01T00:00:00Z",
            updatedAt = "2024-01-01T00:00:00Z",
            v = 1
        ),
        Pet(
            id = "2",
            name = "Whiskers",
            petType = "Cat",
            breed = "Persian",
            dateOfBirth = "2019-08-22",
            gender = "Female",
            photoUrl = "",
            weight = Weight(unit = "kg", value = 4.2),
            ownerId = Owner(
                id = "owner2",
                userId = "user2",
                email = "owner2@example.com",
                fullName = "Jane Smith"
            ),
            identifiers = Identifiers(
                allergies = listOf("Fish"),
                clipLocation = "Right ear",
                colorMarkings = "White with gray patches",
                isNeuteredOrSpayed = true,
                microchipLocation = "Between shoulder blades",
                microchipNumber = "987654321",
                size = "Medium"
            ),
            passport = Passport(schedules = emptyList()),
            medicalRecords = emptyList(),
            petCare = emptyList(),
            petIds = emptyList(),
            createdAt = "2024-01-02T00:00:00Z",
            updatedAt = "2024-01-02T00:00:00Z",
            v = 1
        ),
        Pet(
            id = "3",
            name = "Max",
            petType = "Dog",
            breed = "German Shepherd",
            dateOfBirth = "2021-03-10",
            gender = "Male",
            photoUrl = "",
            weight = Weight(unit = "kg", value = 32.8),
            ownerId = Owner(
                id = "owner3",
                userId = "user3",
                email = "owner3@example.com",
                fullName = "Bob Johnson"
            ),
            identifiers = Identifiers(
                allergies = emptyList(),
                clipLocation = "Left ear",
                colorMarkings = "Black and tan",
                isNeuteredOrSpayed = false,
                microchipLocation = "Between shoulder blades",
                microchipNumber = "456789123",
                size = "Large"
            ),
            passport = Passport(schedules = emptyList()),
            medicalRecords = emptyList(),
            petCare = emptyList(),
            petIds = emptyList(),
            createdAt = "2024-01-03T00:00:00Z",
            updatedAt = "2024-01-03T00:00:00Z",
            v = 1
        ),
        Pet(
            id = "4",
            name = "Luna",
            petType = "Cat",
            breed = "Maine Coon",
            dateOfBirth = "2022-01-08",
            gender = "Female",
            photoUrl = "",
            weight = Weight(unit = "kg", value = 6.1),
            ownerId = Owner(
                id = "owner4",
                userId = "user4",
                email = "owner4@example.com",
                fullName = "Alice Brown"
            ),
            identifiers = Identifiers(
                allergies = listOf("Chicken"),
                clipLocation = "Right ear",
                colorMarkings = "Brown tabby with white paws",
                isNeuteredOrSpayed = true,
                microchipLocation = "Between shoulder blades",
                microchipNumber = "789123456",
                size = "Large"
            ),
            passport = Passport(schedules = emptyList()),
            medicalRecords = emptyList(),
            petCare = emptyList(),
            petIds = emptyList(),
            createdAt = "2024-01-04T00:00:00Z",
            updatedAt = "2024-01-04T00:00:00Z",
            v = 1
        )
    )

}
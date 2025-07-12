package org.jikisan.taily.ui.screens.addpet

object PetConstants {
    val PET_TYPES =
        listOf("Dog", "Cat", "Bird", "Fish", "Rabbit", "Hamster", "Guinea Pig", "Turtle", "Other")

    val DOG_BREEDS = listOf(
        "Labrador", "Golden Retriever", "German Shepherd", "Bulldog",
        "Poodle", "Shih Tzu", "Beagle", "Rottweiler", "Siberian Husky", "Other"
    )

    val CAT_BREEDS = listOf(
        "Persian", "Maine Coon", "Siamese", "British Shorthair",
        "Ragdoll", "Bengal", "Abyssinian", "Russian Blue", "Scottish Fold", "Other"
    )

    val BIRD_BREEDS =
        listOf("Budgie", "Cockatiel", "Canary", "Lovebird", "Parrot", "Finch", "Other")

    val WEIGHT_UNITS = listOf("kg", "lbs", "g", "oz")

    val SIZE_OPTIONS = listOf("Small", "Medium", "Large", "Extra Large")

    val MICROCHIP_LOCATIONS =
        listOf("Neck", "Between shoulder blades", "Left shoulder", "Right shoulder", "Other")

    val CLIP_LOCATIONS = listOf("Left ear", "Right ear", "Both ears", "Tail", "Other")

    val GENDER_OPTIONS = listOf("Male", "Female")
}
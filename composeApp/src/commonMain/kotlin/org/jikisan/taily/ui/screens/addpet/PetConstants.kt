package org.jikisan.taily.ui.screens.addpet

object PetConstants {
    val PET_TYPES = listOf("Axolotl", "Bird", "Budgie", "Canary", "Cat", "Chinchilla", "Chicken", "Cockatiel", "Dog", "Duck", "Ferret", "Fish", "Frog", "Gecko", "Goat", "Guinea Pig", "Hamster", "Hedgehog", "Hermit Crab", "Iguana", "Koi", "Lizard", "Lovebird", "Mouse", "Parrot", "Pig", "Pony", "Rabbit", "Rat", "Snake", "Sugar Glider", "Tarantula", "Tortoise", "Turtle", "Other")

    val PET_BREEDS = mapOf(
        "Dog" to listOf(
            "Beagle", "Bulldog", "Chihuahua", "Cocker Spaniel", "Dachshund", "Doberman", "Golden Retriever",
            "French Bulldog", "German Shepherd", "Golden Retriever", "Labrador Retriever", "Maltese",
            "Pomeranian", "Poodle", "Rottweiler", "Shih Tzu", "Siberian Husky", "Yorkshire Terrier", "Other"
        ),
        "Cat" to listOf(
            "Abyssinian", "American Shorthair", "Bengal", "British Shorthair", "Burmese",
            "Maine Coon", "Persian", "Ragdoll", "Russian Blue", "Scottish Fold", "Siamese", "Sphynx", "Other"
        ),
        "Bird" to listOf(
            "African Grey Parrot", "Budgie (Parakeet)", "Canary", "Cockatiel", "Cockatoo",
            "Finch", "Lovebird", "Macaw", "Parrotlet", "Parakeet", "Other"
        ),
        "Fish" to listOf(
            "Angelfish", "Betta", "Cichlid", "Discus", "Goldfish", "Guppy", "Koi",
            "Molly", "Oscar", "Platy", "Tetra", "Zebrafish", "Other"
        ),
        "Rabbit" to listOf(
            "American Fuzzy Lop", "Dutch", "English Lop", "Flemish Giant", "Holland Lop",
            "Lionhead", "Mini Rex", "Netherland Dwarf", "Other"
        ),
        "Hamster" to listOf(
            "Campbell's Dwarf", "Chinese Hamster", "Roborovski Dwarf",
            "Syrian (Golden) Hamster", "Winter White Dwarf", "Other"
        ),
        "Guinea Pig" to listOf(
            "Abyssinian", "American", "Coronet", "Peruvian", "Silkie", "Teddy", "Texel", "Other"
        ),
        "Turtle" to listOf(
            "Box Turtle", "Eastern Painted Turtle", "Map Turtle", "Musk Turtle",
            "Red-Eared Slider", "Spotted Turtle", "Yellow-Bellied Slider", "Other"
        ),
        "Lizard" to listOf(
            "Bearded Dragon", "Blue-Tongued Skink", "Chameleon", "Gecko", "Green Anole",
            "Iguana", "Monitor Lizard", "Uromastyx", "Other"
        ),
        "Snake" to listOf(
            "Ball Python", "Boa Constrictor", "Corn Snake", "Garter Snake", "Green Tree Python",
            "King Snake", "Milk Snake", "Rosy Boa", "Other"
        ),
        "Hedgehog" to listOf(
            "African Pygmy Hedgehog", "European Hedgehog", "Long-Eared Hedgehog", "Other"
        ),
        "Ferret" to listOf(
            "Standard Ferret", "Angora Ferret", "Black Sable Ferret", "Albino Ferret", "Other"
        ),
        "Chinchilla" to listOf(
            "Standard Grey", "White Mosaic", "Beige", "Ebony", "Other"
        ),
        "Tarantula" to listOf(
            "Chilean Rose", "Mexican Red Knee", "Pink Toe", "Goliath Birdeater", "Other"
        ),
        "Frog" to listOf(
            "American Bullfrog", "Dart Frog", "Pacman Frog", "Tree Frog", "White's Tree Frog", "Other"
        )
        // Species without specific breeds will default to "Other"
    )





    val WEIGHT_UNITS = listOf("kg", "lbs")

    val FRACTIONAL_PARTS_1_DECIMAL = listOf(
        ".0", ".1", ".2", ".3", ".4", ".5", ".6", ".7", ".8", ".9"
    )


    val SIZE_OPTIONS = listOf("Small", "Medium", "Large", "Extra Large")

    val MICROCHIP_LOCATIONS =
        listOf("Neck", "Between shoulder blades", "Left shoulder", "Right shoulder", "Other")

    val CLIP_LOCATIONS = listOf("Left ear", "Right ear", "Both ears", "Tail", "Other")

    val GENDER_OPTIONS = listOf("Male", "Female")
}
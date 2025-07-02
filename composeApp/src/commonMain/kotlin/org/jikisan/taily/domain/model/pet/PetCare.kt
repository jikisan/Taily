package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable
import org.jikisan.taily.domain.model.Weight

data class PetCare(
    val careType: String,
    val clinic: String,
    val groomed: Groomed,
    val groomer: String,
    val groomingDateTime: String,
    val id: String,
    val notes: String,
    val weight: Weight
)
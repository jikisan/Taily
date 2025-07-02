package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.model.Weight

@Serializable
data class PetCare(
    @SerialName("careType")
    val careType: String,
    @SerialName("clinic")
    val clinic: String,
    @SerialName("groomed")
    val groomed: Groomed,
    @SerialName("groomer")
    val groomer: String,
    @SerialName("groomingDateTime")
    val groomingDateTime: String,
    @SerialName("_id")
    val id: String,
    @SerialName("notes")
    val notes: String,
    @SerialName("weight")
    val weight: Weight
)
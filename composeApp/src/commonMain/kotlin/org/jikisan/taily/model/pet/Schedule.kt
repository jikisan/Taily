package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jikisan.taily.model.Weight

@Serializable
data class Schedule(
    @SerialName("given")
    val given: Given,
    @SerialName("hospital")
    val hospital: String,
    @SerialName("_id")
    val id: String,
    @SerialName("notes")
    val notes: String,
    @SerialName("schedDateTime")
    val schedDateTime: String,
    @SerialName("vaccineType")
    val vaccineType: String,
    @SerialName("vet")
    val vet: String,
    @SerialName("weight")
    val weight: Weight
)
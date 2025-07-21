package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable
import org.jikisan.taily.domain.model.Weight

data class Schedule(
    val given: Given,
    val hospital: String,
    val id: String,
    val notes: String,
    val schedDateTime: String,
    val vaccineType: String,
    val vet: String? = "",
    val weight: Weight? = null
)
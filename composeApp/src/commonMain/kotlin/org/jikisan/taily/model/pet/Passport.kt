package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Passport(
    @SerialName("schedules")
    val schedules: List<Schedule>
)
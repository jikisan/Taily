package org.jikisan.taily.model.pet


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PassportDTO(
    @SerialName("schedules")
    val schedules: List<ScheduleDTO>
)
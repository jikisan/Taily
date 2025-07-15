package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable

@Serializable
data class PassportDTO(
    val schedules: List<ScheduleDTO>
)
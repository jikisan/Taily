package org.jikisan.taily.model.pet


import kotlinx.serialization.Serializable

data class Passport(
    val schedules: List<Schedule>
)
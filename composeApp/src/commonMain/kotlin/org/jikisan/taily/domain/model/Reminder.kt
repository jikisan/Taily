package org.jikisan.taily.domain.model

import org.jikisan.taily.domain.model.enum.ReminderType

data class Reminder(
    val id: String,
    val type: String,
    val reminderType: ReminderType,
    val petId: String,
    val petName: String,
)

data class ReminderList(
    val dateTime: String,
    val reminders: List<Reminder>
)


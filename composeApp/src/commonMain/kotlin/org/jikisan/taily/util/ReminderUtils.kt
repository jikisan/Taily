package org.jikisan.taily.util

import org.jikisan.taily.domain.model.Reminder
import org.jikisan.taily.domain.model.ReminderList
import org.jikisan.taily.domain.model.SchedulesList
import org.jikisan.taily.domain.model.enum.ReminderType
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.Schedule

fun sortDateTime(pets: List<Pet>): List<ReminderList> = pets.flatMap { pet ->
    // Collect all reminders for this pet
    val allReminders = mutableListOf<Pair<String, Reminder>>()

    // Add passport schedules
    pet.passport?.schedules?.forEach { schedule ->
        allReminders.add(
            schedule.schedDateTime to Reminder(
                id = schedule.id,
                type = schedule.vaccineType,
                reminderType = ReminderType.PASSPORT,
                petId = pet.id,
                petName = pet.name
            )
        )
    }

    // Add pet care
    pet.petCare?.forEach { care ->
        allReminders.add(
            care.groomingDateTime to Reminder(
                id = care.id,
                type = care.careType,
                reminderType = ReminderType.PETCARE,
                petId = pet.id,
                petName = pet.name
            )
        )
    }

    // Add medical records
    pet.medicalRecords?.forEach { medical ->
        allReminders.add(
            medical.medicalDateTime to Reminder(
                id = medical.id,
                type = medical.medicalType,
                reminderType = ReminderType.MEDICAL,
                petId = pet.id,
                petName = pet.name
            )
        )
    }

    // Group by exact datetime and create ReminderList
    allReminders.groupBy { it.first }
        .map { (dateTime, reminders) ->
            ReminderList(
                dateTime = dateTime,
                reminders = reminders.map { it.second }
            )
        }
}.sortedBy { it.dateTime }

fun sortPassportDateTime(pet: Pet): List<Schedule> {

    return pet.passport.schedules.sortedBy { it.schedDateTime }


//    val passportReminders = mutableListOf<Pair<String, Schedule>>()
//
//    pet.passport.schedules.forEach { schedule ->
//        passportReminders.add(
//            schedule.schedDateTime to schedule
//        )
//    }
//
//    return passportReminders.groupBy { it.first }
//        .map { (dateTime, schedules) ->
//            SchedulesList(
//                dateTime = dateTime,
//                schedules = schedules.map { it.second }
//            )
//        }.sortedBy { it.dateTime }

}


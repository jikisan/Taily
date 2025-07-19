package org.jikisan.taily.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDateTime
import org.jikisan.taily.domain.model.Reminder
import org.jikisan.taily.domain.model.ReminderList
import org.jikisan.taily.domain.model.enum.ReminderType
import org.jikisan.taily.domain.model.pet.Pet
import kotlin.math.abs

object DateUtils {

    private val monthNames = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    private val monthShortNames = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )


    /*ISO PARAMETERS*/

    /**
     * Parses ISO 8601 date string (e.g., "2024-01-01T00:00:00Z") to DateComponents
     */
    private fun parseIsoDate(isoDateString: String): DateComponents? {
        return try {
            val instant = Instant.parse(isoDateString)
            val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            DateComponents(
                year = dateTime.year,
                month = dateTime.monthNumber,
                day = dateTime.dayOfMonth,
                hour = dateTime.hour,
                minute = dateTime.minute,
                second = dateTime.second
            )
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Gets the year from ISO date string
     */
    fun getYear(isoDateString: String): Int? {
        return parseIsoDate(isoDateString)?.year
    }

    /**
     * Gets the month (1-12) from ISO date string
     */
    fun getMonth(isoDateString: String): Int? {
        return parseIsoDate(isoDateString)?.month
    }

    /**
     * Gets the day of month from ISO date string
     */
    fun getDay(isoDateString: String): Int? {
        return parseIsoDate(isoDateString)?.day
    }

    /**
     * Gets the hour (0-23) from ISO date string
     */
    fun getHour(isoDateString: String): Int? {
        return parseIsoDate(isoDateString)?.hour
    }

    /**
     * Gets the minute (0-59) from ISO date string
     */
    fun getMinute(isoDateString: String): Int? {
        return parseIsoDate(isoDateString)?.minute
    }

    /**
     * Gets the second (0-59) from ISO date string
     */
    fun getSecond(isoDateString: String): Int? {
        return parseIsoDate(isoDateString)?.second
    }

    /**
     * Formats ISO date string to readable format (e.g., "Jan 1, 2024")
     */
    fun formatToReadableDate(isoDateString: String): String? {
        val dateComponents = parseIsoDate(isoDateString) ?: return null
        return try {
            val monthName = monthShortNames.getOrNull(dateComponents.month - 1) ?: return null
            "$monthName ${dateComponents.day}, ${dateComponents.year}"
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Formats an ISO date string to "YYYY-MM-DD" format (e.g., "2024-01-01").
     */
    fun formatToYYYYMMDD(isoDateString: String): String? {
        val date = parseIsoDate(isoDateString) ?: return null

        val year = date.year
        val month = date.month.toString().padStart(2, '0')
        val day = date.day.toString().padStart(2, '0')

        return "$year-$month-$day"
    }

    /**
     * Formats an ISO date string to "MM-DD-YYYY" format (e.g., "01-01-2024").
     */
    fun formatToMMDDYYYY(isoDateString: String): String? {
        val date = parseIsoDate(isoDateString) ?: return null

        val month = date.month.toString().padStart(2, '0')
        val day = date.day.toString().padStart(2, '0')
        val year = date.year

        return "$month-$day-$year"
    }


    /**
     * Formats ISO date string to time format (e.g., "10:30 AM")
     */
    fun formatToTime(isoDateString: String): String? {
        val dateComponents = parseIsoDate(isoDateString) ?: return null
        return try {
            val hour12 = if (dateComponents.hour == 0) 12
            else if (dateComponents.hour > 12) dateComponents.hour - 12
            else dateComponents.hour
            val amPm = if (dateComponents.hour < 12) "AM" else "PM"
            "${hour12}:${dateComponents.minute.toString().padStart(2, '0')} $amPm"
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Formats ISO date string to full date time format (e.g., "Jan 1, 2024 at 10:30 AM")
     */
    fun formatToFullDateTime(isoDateString: String): String? {
        val date = formatToReadableDate(isoDateString) ?: return null
        val time = formatToTime(isoDateString) ?: return null
        return "$date at $time"
    }





    /*GENERIC DATE UTILS*/

    /**
     * Calculates age from birth date ISO string (simplified calculation)
     */
    fun calculateAge(birthDateIso: String): Int? {
        val birthDate = parseIsoDate(birthDateIso) ?: return null

        // Simple calculation - would need current date from system
        // For now, using 2024 as current year as an example
        val currentYear = 2024
        val currentMonth = 12
        val currentDay = 1

        var age = currentYear - birthDate.year

        // Adjust if birthday hasn't occurred this year
        if (currentMonth < birthDate.month ||
            (currentMonth == birthDate.month && currentDay < birthDate.day)
        ) {
            age--
        }

        return age
    }

    /**
     * Gets month name from month number (1-12)
     */
    fun getMonthName(monthNumber: Int): String? {
        return monthNames.getOrNull(monthNumber - 1)
    }

    /**
     * Gets short month name from month number (1-12)
     */
    fun getShortMonthName(monthNumber: Int): String? {
        return monthShortNames.getOrNull(monthNumber - 1)
    }

    /**
     * Validates if the ISO date string format is correct
     */
    fun isValidIsoDate(isoDateString: String): Boolean {
        return parseIsoDate(isoDateString) != null
    }

    /**
     * Formats date to display format used in UI (e.g., "May 15, 2020")
     */
    fun formatDateForDisplay(isoDateString: String): String {
        return formatToReadableDate(isoDateString) ?: isoDateString
    }

    /**
     * Formats date to display format used in UI (e.g., "May 15, 2020 - Wednesday")
     */
    fun formatDateForDisplayWithDayOfWeek(date: LocalDate): String {
        val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() } // June
        val day = date.dayOfMonth                            // 1
        val year = date.year                                  // 2001
        val dayOfWeek = date.dayOfWeek.name.lowercase()
            .replaceFirstChar { it.uppercase() }
            .take(3)             // Wednesday
        return "$month $day, $year - $dayOfWeek"
    }

    /**
    Helper function to format LocalDate to 12/31/2000 string
     **/
    fun formatDateToString(date: LocalDate): String {
        val month = date.monthNumber.toString().padStart(2, '0')
        val day = date.dayOfMonth.toString().padStart(2, '0')
        val year = date.year.toString()
        return "$month/$day/$year"
    }

    /**
    Helper function to convert 12/31/2000 string to LocalDate
     **/
    fun convertToISO_MPP(input: String): String {
        val (month, day, year) = input.split("/")
        val date = LocalDate(year.toInt(), month.toInt(), day.toInt())
        return date.atStartOfDayIn(TimeZone.UTC).toString() // ISO 8601 UTC
    }

    /**
     * Calculates the age or time difference from the current date and time.
     * Formats the output like "3 yrs, 2 mo.", "3 yrs", "1 yr", or other appropriate formats.
     */
    fun getAgeOrTimeDifference(isoDateString: String): String? {
        val pastDateComponents = parseIsoDate(isoDateString) ?: return null
        val pastDateTime = try {
            LocalDateTime(
                pastDateComponents.year,
                pastDateComponents.month,
                pastDateComponents.day,
                pastDateComponents.hour,
                pastDateComponents.minute,
                pastDateComponents.second
            )
        } catch (e: Exception) {
            return null // Invalid date components
        }

        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Check if the date is in the future
        if (pastDateTime > currentDateTime) {
            return "Future date"
        }

        // Convert to dates for easier calculation
        val pastDate = pastDateTime.date
        val currentDate = currentDateTime.date

        // Calculate the difference using Period
        val period = pastDate.periodUntil(currentDate)

        return formatAgeDifference(period.years, period.months, period.days)
    }

    private fun formatAgeDifference(years: Int, months: Int, days: Int): String {
        if (years < 0) return "Future date" // Or handle as an error

        val parts = mutableListOf<String>()
        if (years > 0) {
            parts.add("$years yr${if (years > 1) "s" else ""}")
        }
        if (months > 0) {
            parts.add("$months mo${if (months > 1) "s" else ""}")
        }

        return when {
            parts.isNotEmpty() -> parts.joinToString(", ")
            days > 0 -> "$days day${if (days > 1) "s" else ""}"
            else -> "Today" // Or less than a day
        }
    }

    fun getAgeOrTimeDifferencePrecise(isoDateString: String): String? {
        return try {
            // Parse the full ISO string with timezone info
            val instant = Instant.parse(isoDateString)
            val pastDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
            val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

            // Check if the date is in the future
            if (pastDateTime > currentDateTime) {
                return "Future date"
            }

            // For precise age calculation, use the date part and calculate manually
            val pastDate = pastDateTime.date
            val currentDate = currentDateTime.date

            var years = currentDate.year - pastDate.year
            var months = currentDate.monthNumber - pastDate.monthNumber
            var days = currentDate.dayOfMonth - pastDate.dayOfMonth

            // Adjust for negative days
            if (days < 0) {
                months--
                // Get days in the previous month
                val previousMonth = currentDate.minus(1, DateTimeUnit.MONTH)
                val daysInPreviousMonth = getDaysInMonth(previousMonth.year, previousMonth.monthNumber)
                days += daysInPreviousMonth
            }

            // Adjust for negative months
            if (months < 0) {
                years--
                months += 12
            }

            // If we haven't reached the exact birthday yet this year, subtract a year
            if (years > 0) {
                val birthdayThisYear = LocalDate(currentDate.year, pastDate.month, pastDate.dayOfMonth)
                if (currentDate < birthdayThisYear) {
                    years--
                    months = currentDate.monthNumber - pastDate.monthNumber + 12
                    if (currentDate.dayOfMonth < pastDate.dayOfMonth) {
                        months--
                        val prevMonth = currentDate.minus(1, DateTimeUnit.MONTH)
                        days = currentDate.dayOfMonth + getDaysInMonth(prevMonth.year, prevMonth.monthNumber) - pastDate.dayOfMonth
                    } else {
                        days = currentDate.dayOfMonth - pastDate.dayOfMonth
                    }
                }
            }

            return formatAgeDifference(years, months, days)
        } catch (e: Exception) {
            // Fallback to the manual parsing method
            getAgeOrTimeDifference(isoDateString)
        }

    }

    fun getDaysInMonth(year: Int, month: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> throw IllegalArgumentException("Invalid month: $month")
        }
    }

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


    // Helper function to check leap year
    private fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    }

}

/**
 * Data class to hold parsed date components
 */
data class DateComponents(
    val year: Int,
    val month: Int, // 1-12
    val day: Int,   // 1-31
    val hour: Int,  // 0-23
    val minute: Int, // 0-59
    val second: Int  // 0-59
)
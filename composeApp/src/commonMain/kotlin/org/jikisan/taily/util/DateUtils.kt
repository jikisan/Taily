package org.jikisan.taily.util

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

    /**
     * Parses ISO 8601 date string (e.g., "2024-01-01T00:00:00Z") to DateComponents
     */
    fun parseIsoDate(isoDateString: String): DateComponents? {
        return try {
            // Remove 'Z' and split by 'T'
            val cleanDate = isoDateString.replace("Z", "")
            val parts = cleanDate.split("T")

            if (parts.size != 2) return null

            val datePart = parts[0] // "2024-01-01"
            val timePart = parts[1] // "00:00:00" or "00:00:00.000"

            val dateComponents = datePart.split("-")
            val timeComponents = timePart.split(":")

            if (dateComponents.size != 3 || timeComponents.size != 3) return null

            // Handle milliseconds in seconds part
            val secondsPart = timeComponents[2]
            val seconds = if (secondsPart.contains(".")) {
                secondsPart.split(".")[0].toInt()
            } else {
                secondsPart.toInt()
            }

            DateComponents(
                year = dateComponents[0].toInt(),
                month = dateComponents[1].toInt(),
                day = dateComponents[2].toInt(),
                hour = timeComponents[0].toInt(),
                minute = timeComponents[1].toInt(),
                second = seconds
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
     * Formats ISO date string to simple date format (e.g., "2024-01-01")
     */
    fun formatToSimpleDate(isoDateString: String): String? {
        val dateComponents = parseIsoDate(isoDateString) ?: return null
        return try {
            "${dateComponents.year}-${
                dateComponents.month.toString().padStart(2, '0')
            }-${dateComponents.day.toString().padStart(2, '0')}"
        } catch (e: Exception) {
            null
        }
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
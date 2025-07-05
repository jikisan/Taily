package org.jikisan.taily.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vidspark.androidapp.ui.theme.TailyTheme
import kotlinx.datetime.*
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.ui.tooling.preview.Preview

// Data class for event dots
data class EventDot(
    val color: Color,
    val count: Int = 1
)

// Data class for calendar date with events
data class CalendarDate(
    val date: LocalDate,
    val events: List<EventDot> = emptyList()
)

// Color configuration for DateCell
data class DateCellColors(
    val selectedContainerColor: Color,
    val selectedTextColor: Color,
    val pastContainerColor: Color,
    val pastTextColor: Color,
    val futureContainerColor: Color,
    val futureTextColor: Color
)

// Border configuration for DateCell
data class DateCellBorder(
    val selectedBorderColor: Color,
    val pastBorderColor: Color,
    val futureBorderColor: Color,
    val selectedBorderWidth: Dp,
    val pastBorderWidth: Dp,
    val futureBorderWidth: Dp
)

// Elevation configuration for DateCell
data class DateCellElevation(
    val selectedElevation: Dp,
    val pastElevation: Dp,
    val futureElevation: Dp
)

// Default values object
object DateCellDefaults {
    @Composable
    fun colors(
        selectedContainerColor: Color = MaterialTheme.colorScheme.surface,
        selectedTextColor: Color = Color.Black,
        pastContainerColor: Color = Color.Transparent,
        pastTextColor: Color = Color.LightGray,
        futureContainerColor: Color = Color.Transparent,
        futureTextColor: Color = Color.LightGray
    ) = DateCellColors(
        selectedContainerColor = selectedContainerColor,
        selectedTextColor = selectedTextColor,
        pastContainerColor = pastContainerColor,
        pastTextColor = pastTextColor,
        futureContainerColor = futureContainerColor,
        futureTextColor = futureTextColor
    )

    fun border(
        selectedBorderColor: Color = Color.Transparent,
        pastBorderColor: Color = Color.Transparent,
        futureBorderColor: Color = Color.Transparent,
        selectedBorderWidth: Dp = 0.dp,
        pastBorderWidth: Dp = 0.dp,
        futureBorderWidth: Dp = 0.dp
    ) = DateCellBorder(
        selectedBorderColor = selectedBorderColor,
        pastBorderColor = pastBorderColor,
        futureBorderColor = futureBorderColor,
        selectedBorderWidth = selectedBorderWidth,
        pastBorderWidth = pastBorderWidth,
        futureBorderWidth = futureBorderWidth
    )

    fun elevation(
        selectedElevation: Dp = 0.dp,
        pastElevation: Dp = 0.dp,
        futureElevation: Dp = 0.dp
    ) = DateCellElevation(
        selectedElevation = selectedElevation,
        pastElevation = pastElevation,
        futureElevation = futureElevation
    )
}

@OptIn(ExperimentalTime::class)
@Composable
fun RowCalendar(
    modifier: Modifier = Modifier,
    startDate: LocalDate = LocalDate.now(),
    selectedDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit = {},
    events: Map<LocalDate, List<EventDot>> = emptyMap(),
    dateRange: Int = 365, // Number of days to show (past and future)
    content: @Composable (date: LocalDate, isSelected: Boolean, events: List<EventDot>, onClick: () -> Unit) -> Unit
) {
    val listState = rememberLazyListState()
    val today = LocalDate.now()

    // Generate date list centered around startDate
    val dateList = remember(startDate, dateRange) {
        val halfRange = dateRange / 2
        (-halfRange..halfRange).map { offset ->
            CalendarDate(
                date = startDate.plus(offset, DateTimeUnit.DAY),
                events = events[startDate.plus(offset, DateTimeUnit.DAY)] ?: emptyList()
            )
        }
    }

    // Scroll to today's date initially
    LaunchedEffect(startDate) {
        val todayIndex = dateList.indexOfFirst { it.date == today }
        if (todayIndex != -1) {
//            listState.animateScrollToItem(todayIndex)
            listState.scrollToItem(todayIndex - 1)
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(dateList) { calendarDate ->
            content(
                calendarDate.date,
                calendarDate.date == selectedDate,
                calendarDate.events
            ) {
                onDateSelected(calendarDate.date)
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun DateCell(
    date: LocalDate,
    isSelected: Boolean,
    events: List<EventDot> = emptyList(),
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    colors: DateCellColors = DateCellDefaults.colors(),
    border: DateCellBorder = DateCellDefaults.border(),
    elevation: DateCellElevation = DateCellDefaults.elevation()
) {
    val today = LocalDate.now()
    val isPast = date < today
    val isFuture = date > today

    // Determine colors based on state
    val containerColor = when {
        isSelected -> colors.selectedContainerColor
        isPast -> colors.pastContainerColor
        else -> colors.futureContainerColor
    }

    val textColor = when {
        isSelected -> colors.selectedTextColor
        isPast -> colors.pastTextColor
        else -> colors.futureTextColor
    }

    val borderColor = when {
        isSelected -> border.selectedBorderColor
        isPast -> border.pastBorderColor
        else -> border.futureBorderColor
    }

    val borderWidth = when {
        isSelected -> border.selectedBorderWidth
        isPast -> border.pastBorderWidth
        else -> border.futureBorderWidth
    }

    val cardElevation = when {
        isSelected -> elevation.selectedElevation
        isPast -> elevation.pastElevation
        else -> elevation.futureElevation
    }

    Card(
        modifier = modifier
            .width(60.dp)
            .clickable { onClick() }
            .then(
                if (borderWidth > 0.dp) {
                    Modifier.border(borderWidth, borderColor, shape)
                } else Modifier
            ),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Day of week
            Text(
                text = date.dayOfWeek.name.take(3),
                fontSize = 12.sp,
                color = textColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Day number
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Event dots
            if (events.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    events.take(3).forEach { event -> // Limit to 3 dots to avoid overflow
                        repeat(event.count.coerceAtMost(3)) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(event.color, CircleShape)
                            )
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

/**
 * Checks whether the date is before [other] date.
 * @return true if the date is before [other] date, false otherwise.
 */
fun LocalDate.isBefore(other: LocalDate): Boolean {
    return this < other
}

/**
 * Checks whether the date is after [other] date.
 * @return true if the date is after [other] date, false otherwise.
 */
fun LocalDate.isAfter(other: LocalDate): Boolean {
    return this > other
}


/**
 * @return the current date as a [LocalDate] instance in the current system timezone.
 */
fun LocalDate.Companion.now(): LocalDate {
    val currentInstant = Clock.System.now()
    val timeZone = TimeZone.currentSystemDefault()
    return currentInstant.toLocalDateTime(timeZone).date
}


@Preview
@Composable
fun DateCellPreview() {
    TailyTheme {

        val sampleEvents = remember {
            mapOf(
                LocalDate.now() to listOf(
                    EventDot(Color.Red, 2),
                    EventDot(Color.Blue, 1)
                ),
                LocalDate.now().plus(1, DateTimeUnit.DAY) to listOf(
                    EventDot(Color.Green, 1)
                ),
                LocalDate.now().plus(3, DateTimeUnit.DAY) to listOf(
                    EventDot(Color.Yellow, 3)
                )
            )
        }

        var selectedDate by remember { mutableStateOf(LocalDate.now()) }

        Box(
            modifier = Modifier
                .background(Color.White)
        ){
            RowCalendar(
                modifier = Modifier.height(100.dp),
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                },
                events = sampleEvents,
                content = { date, isSelected, events, onClick ->
                    DateCell(
                        date = date,
                        isSelected = isSelected,
                        events = events,
                        onClick = onClick,
                        modifier = Modifier.padding(2.dp),
                        shape = RoundedCornerShape(25.dp),
                        elevation = DateCellDefaults.elevation(
                            selectedElevation = 0.dp,
                            pastElevation = 0.dp,
                            futureElevation = 0.dp
                        ),
                        colors = DateCellDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.surface,
                            selectedTextColor = Color.Black,
                            pastContainerColor = Color.Transparent,
                            pastTextColor = Color.LightGray,
                            futureContainerColor = Color.Transparent,
                            futureTextColor = Color.LightGray
                        ),
                    )
                }
            )
        }


    }
}


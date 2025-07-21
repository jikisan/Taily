package org.jikisan.taily.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vidspark.androidapp.ui.theme.Blue
import com.vidspark.androidapp.ui.theme.OffBlue
import org.jetbrains.compose.resources.painterResource
import org.jikisan.taily.domain.model.enum.FilterType
import org.jikisan.taily.domain.model.enum.FilterType.HISTORY
import org.jikisan.taily.domain.model.enum.ReminderType
import org.jikisan.taily.model.pet.Schedule
import org.jikisan.taily.util.DateUtils
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.arrow_drop_down_24px
import taily.composeapp.generated.resources.pill_24px

@Composable
fun ScheduleItemCard(
    schedule: Schedule,
    modifier: Modifier = Modifier,
    scheduleType: FilterType
) {
    var showOptions by remember { mutableStateOf(false) } // Remember state for recomposition

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = OffBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { showOptions = !showOptions }
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val transition = updateTransition(targetState = showOptions, label = "ArrowTransition")

        val rotationAngle by transition.animateFloat(
            label = "RotationAngle"
        ) { expanded -> if (expanded) 180f else 0f }

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .border(
                        width = 1.dp,
                        color = Blue,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.pill_24px),
                    contentDescription = "Pill icon",
                    tint = Blue,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Text content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = schedule.vaccineType,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    text = schedule.hospital,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Blue
                    )
                )
            }

            // Date and Time
            Column(horizontalAlignment = Alignment.End) {
                val utcDate = schedule.schedDateTime.trim()
                val readableDate = DateUtils.formatToReadableDate(utcDate)
                val time = DateUtils.formatToTime(utcDate)

                readableDate?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                    )
                }

                time?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2C2C2C)
                        )
                    )
                }
            }


        }

        // Dropdown Arrow Icon (animated)
        Icon(
            painter = painterResource(Res.drawable.arrow_drop_down_24px),
            contentDescription = "Dropdown icon",
            tint = Color(0xFF2C2C2C),
            modifier = Modifier
                .size(24.dp)
                .padding(start = 4.dp)
                .graphicsLayer {
                    rotationZ = rotationAngle
                }
        )
    }

    AnimatedVisibility(
        visible = showOptions,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = OffBlue,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.weight(1f) // Equal width
            ) {
                Text(
                    text = "Edit",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }

            if (scheduleType == FilterType.UPCOMING) {
                FilledTonalButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f), // Equal width
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = Blue)
                ) {
                    Text(
                        text = "Complete",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }


        }

    }
}


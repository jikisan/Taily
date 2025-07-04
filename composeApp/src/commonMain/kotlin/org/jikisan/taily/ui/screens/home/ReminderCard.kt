package org.jikisan.taily.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vidspark.androidapp.ui.theme.Blue
import com.vidspark.androidapp.ui.theme.LightGreenBackground
import com.vidspark.androidapp.ui.theme.LightOrangeBackground
import com.vidspark.androidapp.ui.theme.OffBlue
import com.vidspark.androidapp.ui.theme.SoftGreen
import com.vidspark.androidapp.ui.theme.SoftOrange
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jikisan.taily.domain.model.Reminder
import org.jikisan.taily.domain.model.ReminderType
import org.jikisan.taily.model.pet.Schedule
import org.jikisan.taily.util.DateUtils
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.content_cut_24px
import taily.composeapp.generated.resources.pill_24px
import taily.composeapp.generated.resources.stethoscope_24px

@Composable
fun ReminderCard(
    reminder: Reminder,
    modifier: Modifier = Modifier
) {

    var icon: DrawableResource
    var surface: Color
    var primary: Color

    when (reminder.reminderType) {
        ReminderType.PASSPORT -> {
            icon = Res.drawable.pill_24px
            surface = OffBlue
            primary = Blue
        }
        ReminderType.PETCARE -> {
            icon =Res.drawable.content_cut_24px
            surface = LightGreenBackground
            primary = SoftGreen
        }
        ReminderType.MEDICAL -> {
            icon = Res.drawable.stethoscope_24px
            surface = LightOrangeBackground
            primary = SoftOrange
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = surface, shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Text content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = reminder.type,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            Text(
                text = reminder.petName,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = primary
                )
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(48.dp)
                .border(
                    width = 1.dp, // Thickness of colored corners
                    color = primary, // Corner color
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Pill icon",
                tint = primary, // Or keep white
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
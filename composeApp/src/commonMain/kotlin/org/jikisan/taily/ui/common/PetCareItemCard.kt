package org.jikisan.taily.ui.common

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
import org.jetbrains.compose.resources.painterResource
import org.jikisan.taily.model.pet.PetCare
import org.jikisan.taily.util.DateUtils
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.content_cut_24px

@Composable
fun PetCareItemCard(
    petCare: PetCare,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with background
        Box(
            modifier = Modifier
                .size(48.dp)
                .border(
                    width = 1.dp, // Thickness of colored corners
                    color = MaterialTheme.colorScheme.primary, // Corner color
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.content_cut_24px),
                contentDescription = "Pill icon",
                tint = MaterialTheme.colorScheme.primary, // Or keep white
                modifier = Modifier.size(24.dp)
            )
        }


        Spacer(modifier = Modifier.width(12.dp))

        // Text content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = petCare.careType,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            Text(
                text = petCare.clinic,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }

        // Date and Time
        Column(horizontalAlignment = Alignment.End) {

            val utcDate = petCare.groomingDateTime.trim()
            val readableDate = DateUtils.formatToReadableDate(utcDate)
            val time = DateUtils.formatToTime(utcDate)

            readableDate?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray
                    )
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
}
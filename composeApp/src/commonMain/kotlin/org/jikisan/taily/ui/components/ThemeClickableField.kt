package org.jikisan.taily.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ThemeClickableField(
    value: String,
    label: String = "",
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    borderThickness: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    cornerRadius: Dp = 10.dp,
    isError: Boolean = false,
    errorMessage: String = "",
    onClick: () -> Unit
) {
    val currentBorderColor = if (isError) MaterialTheme.colorScheme.error else borderColor

    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = borderThickness,
                    color = currentBorderColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .clip(RoundedCornerShape(cornerRadius))
                .clickable { onClick() }
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                leadingIcon?.invoke()

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = if (leadingIcon != null) 8.dp else 0.dp)
                ) {
                    // Label
                    if (label.isNotEmpty()) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Value or placeholder
                    Text(
                        text = if (value.isEmpty()) placeholder else value,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (value.isEmpty()) Color.LightGray else MaterialTheme.colorScheme.onSurface
                    )
                }

                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    trailingIcon()
                }
            }
        }

        // Error message
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}
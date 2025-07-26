package org.jikisan.taily.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ThemeOutlineTextField(
    value: String,
    label: String = "",
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier,
    borderThickness: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    cornerRadius: Dp = 10.dp,
    maxLength: Int = 20,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    errorMessage: String = "Maximum character limit reached",
    isError: Boolean = false,
    showCharacterCounter: Boolean = true,
) {
    val focusRequester = remember { FocusRequester() }
    val currentBorderColor = if (isError) MaterialTheme.colorScheme.error else borderColor

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = borderThickness,
                    color = currentBorderColor,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .clip(RoundedCornerShape(cornerRadius))
                .focusRequester(focusRequester)
                .focusable()
                .padding(2.dp)
        ) {
            OutlinedTextField(
                label = { Text(text = label, color = borderColor) },
                value = value,
                placeholder = { Text(text = placeholder, color = Color.LightGray) },
                onValueChange = { newValue ->
                    if (newValue.length <= maxLength) {
                        onValueChange(newValue)
                    }
                },
                readOnly = readOnly,
                modifier = modifier
                    .fillMaxWidth()
                    .then(if (readOnly) Modifier.focusable(false) else Modifier), // Add this line
                shape = RoundedCornerShape(cornerRadius),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedPlaceholderColor = Color.LightGray
                ),
                singleLine = singleLine,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                maxLines = maxLines
            )
        }

        // Character counter and error message row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Error message
            if (isError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Character counter
            if (showCharacterCounter) {
                Text(
                    text = "${value.length}/$maxLength",
                    color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewThemeTextField() {
    TailyTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            ThemeOutlineTextField(
                value = "",
                placeholder = "Placeholder",
                onValueChange = {},
            )
        }

    }

}

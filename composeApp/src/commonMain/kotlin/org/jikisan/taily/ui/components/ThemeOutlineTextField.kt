package org.jikisan.taily.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier,
    borderThickness: Dp = 3.dp,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    cornerRadius: Dp = 10.dp
) {
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = borderThickness,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .focusRequester(focusRequester)
            .focusable()
            .padding(2.dp)
    ) {
        OutlinedTextField(
            value = value,
            placeholder = { Text(placeholder) },
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(cornerRadius),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedPlaceholderColor = Color.LightGray
            ),
            singleLine = true,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            readOnly = readOnly
        )
    }
}


@Preview
@Composable
fun PreviewThemeTextField() {
    TailyTheme {
        Box(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background).padding(16.dp)
        ) {
            ThemeOutlineTextField(
                value = "",
                placeholder = "Placeholder",
                onValueChange = {},
            )
        }

    }

}

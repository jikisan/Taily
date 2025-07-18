package org.jikisan.taily.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DotSeparator(
    modifier: Modifier = Modifier.padding(horizontal = 16.dp),
    color: Color = Color.Gray,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = "·",
        style = style,
        color = color,
        modifier = modifier,
        fontWeight = fontWeight
    )
}
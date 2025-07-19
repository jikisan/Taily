package org.jikisan.taily.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.sad_cat

@Composable
fun EmptyScreen(
    message: String = "Empty",
    resource: DrawableResource = Res.drawable.sad_cat,
    modifier: Modifier = Modifier.fillMaxSize().padding(bottom = 150.dp)
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(resource),
            contentDescription = "Error Icon",
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    }
}
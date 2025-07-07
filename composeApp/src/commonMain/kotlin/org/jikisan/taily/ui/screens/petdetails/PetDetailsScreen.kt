package org.jikisan.taily.ui.screens.petdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.jetbrains.compose.resources.painterResource
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.arrow_back_ios_new_24px
import taily.composeapp.generated.resources.stethoscope_24px

@Composable
fun PetDetailsScreen(
    petId: String,
    navHost: NavHostController,
    topPadding: Dp
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding, start = 8.dp, end = 8.dp),
        contentAlignment = Alignment.Center
    ) {

        IconButton(
            onClick = { navHost.popBackStack() },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(Res.drawable.arrow_back_ios_new_24px),
                contentDescription = "Back"
            )
        }

        Text(text = "Pet Details: $petId")
    }
}
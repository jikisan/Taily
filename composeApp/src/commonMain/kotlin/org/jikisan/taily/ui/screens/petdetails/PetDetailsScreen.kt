package org.jikisan.taily.ui.screens.petdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.arrow_back_ios_new_24px
import taily.composeapp.generated.resources.stethoscope_24px

@Composable
fun PetDetailsScreen(
    petId: String,
    navHost: NavHostController,
    topPadding: Dp,
    viewModel: PetDetailsViewModel = koinViewModel<PetDetailsViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPetDetails(petId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Box(
            modifier = Modifier.height(350.dp).fillMaxWidth()

        ) {
            AsyncImage(
                model = uiState.pet?.photo?.url,
                contentDescription = "Pet Details",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 30.dp,
                            bottomEnd = 30.dp
                        )
                    ),
                contentScale = ContentScale.Crop,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .padding(top = topPadding + 8.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                customButton(
                    onClick = { navHost.popBackStack() },
                    icon = Res.drawable.arrow_back_ios_new_24px
                )
                customButton(
                    onClick = {  },
                    icon = Icons.Default.Edit
                )

            }
        }
    }

}

@Composable
fun customButton(
    onClick: () -> Unit,
    icon: DrawableResource,
) {

    Box(
        modifier = Modifier
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(5.dp)
        ).clip(
            shape = RoundedCornerShape(5.dp)
        ).clickable(
            onClick = onClick
        ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            ).padding(8.dp)
        )
    }
}

@Composable
fun customButton(
    onClick: () -> Unit,
    icon: ImageVector,
) {

    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(5.dp)
            ).clip(
                shape = RoundedCornerShape(5.dp)
            ).clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            ).padding(8.dp)
        )
    }
}

@Preview
@Composable
fun PetDetailsScreenPreview() {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {


        PetDetailsScreen(petId = "1", navHost = rememberNavController(), topPadding = 0.dp)
    }
}
package org.jikisan.taily.ui.screens.petcare

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import org.jikisan.taily.ui.screens.pet.PetViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PetCareScreen(
    petId: String,
    navHost: NavHostController,
    topPadding: Dp,
    viewModel: PetViewModel = koinViewModel<PetViewModel>()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        Text(
            text = "Pet Care: $petId",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
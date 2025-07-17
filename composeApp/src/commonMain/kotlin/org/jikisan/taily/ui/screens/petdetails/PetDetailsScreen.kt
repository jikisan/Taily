package org.jikisan.taily.ui.screens.petdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.model.enum.GenderType
import org.jikisan.taily.ui.common.LoadingScreen
import org.jikisan.taily.ui.components.GenderIcon
import org.jikisan.taily.ui.components.GradientSnackbar
import org.jikisan.taily.util.DateUtils.getAgeOrTimeDifferencePrecise
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.arrow_back_ios_new_24px

@Composable
fun PetDetailsScreen(
    petId: String,
    navHost: NavHostController,
    topPadding: Dp,
    viewModel: PetDetailsViewModel = koinViewModel<PetDetailsViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPetDetails(petId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding),
        verticalArrangement = Arrangement.Top
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                painter = painterResource(Res.drawable.arrow_back_ios_new_24px),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        onClick = { navHost.popBackStack() }
                    )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(
                            onClick = { }
                        ),
                )

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(
                            onClick = { showDeleteDialog = true }
                        ),
                )
            }


        }

        uiState.pet?.let { pet ->
            // Profile picture
            Box(
                modifier = Modifier.height(150.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = pet.photo.url,
                    contentDescription = "Pet Profile Picture",
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .aspectRatio(1f / 1f),
                    contentScale = ContentScale.Crop,
                )
            }

            // Pet Name
            val petName = pet.name

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = petName.capitalize(LocaleList.current),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(modifier = Modifier.height(8.dp))

                GenderIcon(pet = pet, size = 30.dp)

            }

            // Pet Info
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                val petDetailsItems = mutableListOf<String?>()
                petDetailsItems.add(pet.breed.ifBlank { pet.petType })
                pet.dateOfBirth.let {
                    getAgeOrTimeDifferencePrecise(it)?.let { age ->
                        petDetailsItems.add(age)
                    }
                }
//            pet.gender?.let { petDetailsItems.add(it) }
                pet.weight.let { petDetailsItems.add("${it.value} ${it.unit}") }

                petDetailsItems.filterNotNull().forEachIndexed { index, item ->
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.LightGray,
                        fontWeight = FontWeight.Bold
                    )
                    if (index < petDetailsItems.filterNotNull().size - 1) {
                        DotSeparator()
                    }
                }
            }
        }


    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(text = "Delete Pet") },
            text = { Text(text = "Are you sure you want to delete this pet?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deletePet(petId, onSuccess = { navHost.popBackStack() })
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    if (uiState.isDeleting) {
        LoadingScreen()
    }

    if (uiState.isDeletingSuccess) {
        uiState.deleteSuccessMessage?.let {
            GradientSnackbar(message = it)
        }
    }

}

@Composable
fun DotSeparator() {
    Text(
        text = "·",
        style = MaterialTheme.typography.bodyLarge,
        color = Color.Gray,
        modifier = Modifier.padding(horizontal = 16.dp),
        fontWeight = FontWeight.Bold
    )
}

@Preview
@Composable
fun PetDetailsScreenPreview() {

    TailyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PetDetailsScreen(
                petId = "1",
                navHost = rememberNavController(),
                topPadding = 0.dp
            )
        }
    }

}
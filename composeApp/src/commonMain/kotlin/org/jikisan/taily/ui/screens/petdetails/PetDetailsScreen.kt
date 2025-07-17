package org.jikisan.taily.ui.screens.petdetails

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.common.EmptyScreen
import org.jikisan.taily.ui.common.ErrorScreen
import org.jikisan.taily.ui.common.LoadingScreen
import org.jikisan.taily.ui.components.GenderIcon
import org.jikisan.taily.ui.components.GradientSnackbar
import org.jikisan.taily.util.DateUtils.getAgeOrTimeDifferencePrecise
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.arrow_back_ios_new_24px
import taily.composeapp.generated.resources.sad_cat

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

    when {

        uiState.isLoading && uiState.pet == null -> {
            LoadingScreen()
        }

        uiState.errorMessage != null && uiState.pet == null -> {
            ErrorScreen(
                errorMessage = uiState.errorMessage,
                onClick = { viewModel.loadPetDetails(petId) }
            )
        }

        uiState.pet == null -> {
            EmptyScreen("Failed to load pet", Res.drawable.sad_cat)
        }

        else -> {
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
                        pet.weight.let { petDetailsItems.add("${it.value} ${it.unit}") }

                        petDetailsItems.filterNotNull().forEachIndexed { index, item ->
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            if (index < petDetailsItems.filterNotNull().size - 1) {
                                DotSeparator()
                            }
                        }
                    }

                    // Identifiers
                    // Usage in your composable:
                     PetIdentifiersSection(
                         pet = pet,
                         modifier = Modifier.padding(top = 32.dp)
                     )

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
fun PetIdentifiersSection(
    pet: Pet,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        Text(
            text = "Identifiers",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { heading() }
        )

        // Identifiers Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Microchip Information Group
                pet.identifiers.microchipNumber?.let { microchipNumber ->
                    PetInfoSection(
                        title = "Microchip Information",
                        items = buildList {
                            add(
                                PetInfoItem(
                                    label = "Microchip Number",
                                    value = microchipNumber.ifBlank { "-" },
                                    icon = Icons.Filled.Info
                                )
                            )
                            pet.identifiers.microchipLocation?.let { location ->
                                add(
                                    PetInfoItem(
                                        label = "Microchip Location",
                                        value = location.ifBlank { "-" },
                                        icon = Icons.Filled.Place
                                    )
                                )
                            }
                        }
                    )

                    if (pet.identifiers.clipLocation != null ||
                        pet.identifiers.size != null ||
                        pet.identifiers.colorMarkings != null) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                        )
                    }
                }

                // Physical Identifiers Group
                val physicalItems = buildList {
                    pet.identifiers.clipLocation?.let {
                        add(
                            PetInfoItem(
                                label = "Clip Location",
                                value = it.ifBlank { "None" },
                                icon = Icons.Filled.Create
                            )
                        )
                    }
                    pet.identifiers.size?.let {
                        add(
                            PetInfoItem(
                                label = "Size",
                                value = it.ifBlank { "None" },
                                icon = Icons.Filled.Build
                            )
                        )
                    }
                    pet.identifiers.colorMarkings?.let {
                        add(
                            PetInfoItem(
                                label = "Color Markings",
                                value = it.ifBlank { "-" },
                                icon = Icons.Filled.Star
                            )
                        )
                    }
                }

                if (physicalItems.isNotEmpty()) {
                    PetInfoSection(
                        title = "Physical Identifiers",
                        items = physicalItems
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                    )
                }

                // Health Information Group
                PetInfoSection(
                    title = "Health Information",
                    items = listOf(
                        PetInfoItem(
                            label = "Neutered/Spayed",
                            value = if (pet.identifiers.isNeuteredOrSpayed == true) "Yes" else "No",
                            icon = Icons.Filled.Favorite,
                            valueColor = if (pet.identifiers.isNeuteredOrSpayed == true)
                                MaterialTheme.colorScheme.primary else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        PetInfoItem(
                            label = "Allergies",
                            value = if (pet.identifiers.allergies.isEmpty()) "-" else
                                pet.identifiers.allergies.joinToString(", "),
                            icon = Icons.Default.Warning,
                            valueColor = if (pet.identifiers.allergies.isNotEmpty())
                                MaterialTheme.colorScheme.error else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                )
            }
        }
    }
}

@Composable
private fun PetInfoSection(
    title: String,
    items: List<PetInfoItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.semantics { heading() }
        )

        items.forEach { item ->
            ModernPetInfoRow(item = item)
        }
    }
}

@Composable
private fun ModernPetInfoRow(
    item: PetInfoItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = "${item.label}: ${item.value}"
            },
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = item.value,
                style = MaterialTheme.typography.bodyLarge,
                color = item.valueColor,
                fontWeight = if (item.isImportant) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

data class PetInfoItem(
    val label: String,
    val value: String,
    val icon: ImageVector,
    val valueColor: Color = Color.Unspecified,
    val isImportant: Boolean = false
)

@Composable
fun PetInfoRow(
    title: String,
    content: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            modifier = Modifier.widthIn(min = 130.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
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
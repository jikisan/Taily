package org.jikisan.taily.ui.screens.petdetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.vidspark.androidapp.ui.theme.Blue
import com.vidspark.androidapp.ui.theme.SoftGreen
import com.vidspark.androidapp.ui.theme.SoftOrange
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.common.EmptyScreen
import org.jikisan.taily.ui.common.ErrorScreen
import org.jikisan.taily.ui.common.LoadingScreen
import org.jikisan.taily.ui.components.AnimatedGradientSnackbarHost
import org.jikisan.taily.ui.components.DotSeparator
import org.jikisan.taily.ui.components.GenderIcon
import org.jikisan.taily.ui.components.GradientSnackbar
import org.jikisan.taily.ui.components.SnackbarType
import org.jikisan.taily.ui.navigation.NavigationItem
import org.jikisan.taily.util.DateUtils.formatDateForDisplay
import org.jikisan.taily.util.DateUtils.getAgeOrTimeDifferencePrecise
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.allergy_24px
import taily.composeapp.generated.resources.arrow_back_ios_new_24px
import taily.composeapp.generated.resources.cake_24px
import taily.composeapp.generated.resources.calendar_month_24px
import taily.composeapp.generated.resources.clip_24px
import taily.composeapp.generated.resources.colors_24px
import taily.composeapp.generated.resources.hourglass_top_24px
import taily.composeapp.generated.resources.id_card_24px
import taily.composeapp.generated.resources.microchip_24px
import taily.composeapp.generated.resources.microchip_loc_24px
import taily.composeapp.generated.resources.passport_24px
import taily.composeapp.generated.resources.paw_24px
import taily.composeapp.generated.resources.qr_code_24px
import taily.composeapp.generated.resources.ruler_24px
import taily.composeapp.generated.resources.sad_cat
import taily.composeapp.generated.resources.scissors_24px
import taily.composeapp.generated.resources.self_care_24px
import taily.composeapp.generated.resources.stethoscope_24px
import taily.composeapp.generated.resources.weight_24px

data class PetInfoItem(
    val label: String,
    val value: String,
    val drawable: DrawableResource,
    val valueColor: Color = Color.Unspecified,
    val isImportant: Boolean = false
)

@Composable
fun PetDetailsScreen(
    petId: String,
    navHost: NavHostController,
    topPadding: Dp,
    viewModel: PetDetailsViewModel = koinViewModel<PetDetailsViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val navBackStackEntry = navHost.currentBackStackEntryAsState().value

    LaunchedEffect(navBackStackEntry) {
        viewModel.loadPetDetails(petId)
    }

    LaunchedEffect(Unit) {
        viewModel.loadPetDetails(petId)
    }

    when {

        uiState.isLoading && uiState.pet == null -> {
            LoadingScreen(0.dp)
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
                    .padding(top = topPadding)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    IconButton(
                        onClick = { navHost.popBackStack() }
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.arrow_back_ios_new_24px),
                            contentDescription = "Back Icon",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {


                        IconButton(
                            onClick = { showSnackbar = true }
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.qr_code_24px),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                navHost.navigate(
                                    NavigationItem.EditPet.route.replace(
                                        "{petId}",
                                        petId
                                    )
                                )
                            })
                        {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Pet Icon",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }

                    }

                }

                uiState.pet?.let { pet ->
                    // Profile picture
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .height(150.dp)
                            .aspectRatio(1f) // ensure it's square
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .padding(6.dp) // gap between border and image
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        AsyncImage(
                            model = pet.photo.url,
                            contentDescription = "Pet Profile Picture",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
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

                    val quickActions = listOf(
                        QuickAction(
                            label = "IDs",
                            iconRes = Res.drawable.id_card_24px,
                            color = MaterialTheme.colorScheme.primary,
                            onClick = {
                                navHost.navigate(
                                    NavigationItem.PetIds.route.replace("{petId}", petId)
                                )
                            }
                        ),
                        QuickAction(
                            label = "Passport",
                            iconRes = Res.drawable.passport_24px,
                            color = Blue,
                            onClick = {
                                navHost.navigate(
                                    NavigationItem.PetPassport.route.replace("{petId}", petId)
                                )
                            }
                        ),
                        QuickAction(
                            label = "Care",
                            iconRes = Res.drawable.self_care_24px,
                            color = SoftGreen,
                            onClick = {
                                navHost.navigate(
                                    NavigationItem.PetCare.route.replace("{petId}", petId)
                                )
                            }
                        ),
                        QuickAction(
                            label = "Medical",
                            iconRes = Res.drawable.stethoscope_24px,
                            color = SoftOrange,
                            onClick = {
                                navHost.navigate(
                                    NavigationItem.PetMedical.route.replace("{petId}", petId)
                                )
                            }
                        ),
                    )


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        quickActions.forEach { action ->
                            QuickActionButton(action = action)
                        }
                    }


                    // Identifiers
                    PetIdentifiersSection(
                        pet = pet,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    // Delete Button
                    DeletePetButton(onClick = { showDeleteDialog = true })


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
            GradientSnackbar(message = it, type = SnackbarType.SUCCESS)
        }
    }

    if (showSnackbar) {
        AnimatedGradientSnackbarHost(
            message = "QR Code will be available soon",
            type = SnackbarType.INFO,
            onDismiss = { showSnackbar = false },
        )
    }

}

data class QuickAction(
    val label: String,
    val iconRes: DrawableResource,
    val color: Color,
    val onClick: () -> Unit
)


@Composable
fun QuickActionButton(
    action: QuickAction,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(80.dp)
            .clickable { action.onClick() },
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(action.iconRes),
                    contentDescription = action.label,
                    modifier = Modifier.size(24.dp),
                    tint = action.color
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = action.label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
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
            .fillMaxSize()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Section Header
//        Text(
//            text = "Identifiers",
//            style = MaterialTheme.typography.headlineSmall,
//            fontWeight = FontWeight.Bold,
//            color = MaterialTheme.colorScheme.onSurface,
//            modifier = Modifier
//                .fillMaxWidth()
//                .semantics { heading() }
//        )

        // Identifiers Card
        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                //Birthdate Information Group
                pet.dateOfBirth.let { birthdate ->
                    PetInfoSection(
                        title = "About ${pet.name}",
                        items = buildList {

                            add(
                                PetInfoItem(
                                    label = if (pet.breed.isBlank()) "Species" else "Breed",
                                    value = pet.breed.ifBlank { pet.petType },
                                    drawable = Res.drawable.paw_24px
                                )
                            )

                            getAgeOrTimeDifferencePrecise(pet.dateOfBirth)?.let { age ->
                                add(
                                    PetInfoItem(
                                        label = "Age",
                                        value = age,
                                        drawable = Res.drawable.hourglass_top_24px
                                    )
                                )
                            }

                            add(
                                PetInfoItem(
                                    label = "Weight",
                                    value = "${pet.weight.value} ${pet.weight.unit}",
                                    drawable = Res.drawable.weight_24px
                                )
                            )

                            add(
                                PetInfoItem(
                                    label = "Birth date",
                                    value = formatDateForDisplay(birthdate),
                                    drawable = Res.drawable.cake_24px
                                )
                            )


                        }
                    )
                }

                if (pet.identifiers.microchipNumber != null ||
                    pet.identifiers.microchipLocation != null
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                    )
                }

                // Microchip Information Group
                pet.identifiers.microchipNumber?.let { microchipNumber ->
                    PetInfoSection(
                        title = "Microchip Information",
                        items = buildList {
                            add(
                                PetInfoItem(
                                    label = "Microchip Number",
                                    value = microchipNumber.ifBlank { "No Microchip" },
                                    drawable = Res.drawable.microchip_24px
                                )
                            )
                            pet.identifiers.microchipLocation?.let { location ->
                                add(
                                    PetInfoItem(
                                        label = "Microchip Location",
                                        value = location.ifBlank { "No Microchip" },
                                        drawable = Res.drawable.microchip_loc_24px
                                    )
                                )
                            }
                        }
                    )

                    if (pet.identifiers.clipLocation != null ||
                        pet.identifiers.size != null ||
                        pet.identifiers.colorMarkings != null
                    ) {
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
                                value = it.ifBlank { "No Clip" },
                                drawable = Res.drawable.clip_24px
                            )
                        )
                    }
                    pet.identifiers.size?.let {
                        add(
                            PetInfoItem(
                                label = "Size",
                                value = it.ifBlank { "None" },
                                drawable = Res.drawable.ruler_24px
                            )
                        )
                    }
                    pet.identifiers.colorMarkings?.let {
                        add(
                            PetInfoItem(
                                label = "Color Markings",
                                value = it.ifBlank { "-" },
                                drawable = Res.drawable.colors_24px
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
                            drawable = Res.drawable.scissors_24px,
                            valueColor = if (pet.identifiers.isNeuteredOrSpayed == true)
                                MaterialTheme.colorScheme.primary else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        PetInfoItem(
                            label = "Allergies",
                            value = if (pet.identifiers.allergies.isEmpty()) "No allergies" else
                                pet.identifiers.allergies.joinToString(", "),
                            drawable = Res.drawable.allergy_24px,
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
            color = MaterialTheme.colorScheme.onSurface,
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
        Box(
            modifier = Modifier
                .size(40.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(item.drawable),
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = item.value,
                style = MaterialTheme.typography.bodyLarge,
                color = item.valueColor,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = item.label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

        }
    }
}

@Composable
private fun DeletePetButton(onClick: () -> Unit) {

    OutlinedButton(
        onClick = { onClick() }, // Fixed: call onClick properly
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.error
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error) // ✅ Override default border
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = "Delete Pet Icon",
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = "Delete Pet",
            color = MaterialTheme.colorScheme.error
        )
    }

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
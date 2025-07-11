package org.jikisan.taily.ui.screens.addpet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vidspark.androidapp.ui.theme.TailyTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.data.local.mockdata.MockData
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.domain.pet.PetRepository
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.AddPetViewModel
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddNameContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddPetProfilePhotoContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.ChoosePetGenderContent
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.arrow_back_ios_new_24px
import taily.composeapp.generated.resources.happy_pet
import taily.composeapp.generated.resources.loading_paw

val PET_TYPES =
    listOf("Dog", "Cat", "Bird", "Fish", "Rabbit", "Hamster", "Guinea Pig", "Turtle", "Other")
val DOG_BREEDS = listOf(
    "Labrador",
    "Golden Retriever",
    "German Shepherd",
    "Bulldog",
    "Poodle",
    "Shih Tzu",
    "Beagle",
    "Rottweiler",
    "Siberian Husky",
    "Other"
)
val CAT_BREEDS = listOf(
    "Persian",
    "Maine Coon",
    "Siamese",
    "British Shorthair",
    "Ragdoll",
    "Bengal",
    "Abyssinian",
    "Russian Blue",
    "Scottish Fold",
    "Other"
)
val BIRD_BREEDS = listOf("Budgie", "Cockatiel", "Canary", "Lovebird", "Parrot", "Finch", "Other")
val WEIGHT_UNITS = listOf("kg", "lbs", "g", "oz")
val SIZE_OPTIONS = listOf("Small", "Medium", "Large", "Extra Large")
val MICROCHIP_LOCATIONS =
    listOf("Neck", "Between shoulder blades", "Left shoulder", "Right shoulder", "Other")
val CLIP_LOCATIONS = listOf("Left ear", "Right ear", "Both ears", "Tail", "Other")
val GENDER_OPTIONS = listOf("Male", "Female")

val CURRENT_PAGE_HEADER = listOf(
    "Pet's Name",
    "Pet's Profile Photo",
    "Pet's Gender",
    "Pet's Date of Birth",
    "Pet's species",
    "Pet's Breed",
    "Pet's Weight",
    "Identifiers"
)

@Composable
fun AddPetScreen(
    navHost: NavHostController,
    topPadding: Dp,
    viewModel: AddPetViewModel = koinViewModel<AddPetViewModel>()
) {
    val totalPages = CURRENT_PAGE_HEADER.size
    val pagerState = rememberPagerState(pageCount = { totalPages })
    val coroutineScope = rememberCoroutineScope()
    val currentPageHeader = remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(top = topPadding),
    ) {
        Text(
            text = "Add Pet Profile",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth().offset(y = 16.dp)
                .padding(top = 16.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            onClick = {
                if (pagerState.currentPage <= 0) {
                    navHost.popBackStack()
                } else {

                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }

                }
            },
        ) {
            Icon(
                painter = painterResource(Res.drawable.arrow_back_ios_new_24px),
                contentDescription = "Back"
            )
        }

        Text(
            text = CURRENT_PAGE_HEADER[pagerState.currentPage],
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth().offset(y = (-16).dp),
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        LinearProgressIndicator(
            progress = { (pagerState.currentPage.toFloat() + 1) / totalPages.toFloat() },
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surface
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page: Int ->


            when (page) {
                0 -> AddNameContent(viewModel, uiState.pet)
                1 -> AddPetProfilePhotoContent(viewModel, uiState.pet)
                2 -> ChoosePetGenderContent(viewModel, pet = uiState.pet)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                modifier = Modifier.fillMaxWidth(),
                elevation = ButtonDefaults.buttonElevation(15.dp),
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < totalPages - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            // Submit
                        }
                    }
                }) {
                Text(if (pagerState.currentPage + 1 == totalPages) "Submit" else "Next")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedPhysicalPage(viewModel: AddPetViewModel) {
    val pet = viewModel.uiState.value.pet
    // Control menus
    var showPetTypeDropdown by remember { mutableStateOf(false) }
    var showBreedDropdown by remember { mutableStateOf(false) }
    var showWeightUnitDropdown by remember { mutableStateOf(false) }
    var showSizeDropdown by remember { mutableStateOf(false) }

    // Controlled states linked to pet
    val petTypeState = remember { mutableStateOf(pet?.petType ?: "") }
    val breedState = remember { mutableStateOf(pet?.breed ?: "") }
    val weightValueState = remember { mutableStateOf(pet?.weight?.value ?: 0.0) }
    val weightUnitState = remember { mutableStateOf(pet?.weight?.unit ?: WEIGHT_UNITS[0]) }
    val sizeState = remember { mutableStateOf(pet?.identifiers?.size ?: "") }
    val markingsState = remember { mutableStateOf(pet?.identifiers?.colorMarkings ?: "") }

    val containerShape = MaterialTheme.shapes.medium
    val fieldPadding = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Breed & Physical Info",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            shape = containerShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Pet Type Dropdown+Field
                Text(text = "Pet Type", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(6.dp))
                FilledTonalButton(
                    onClick = { showPetTypeDropdown = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = containerShape
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.happy_pet),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (petTypeState.value.isBlank()) "Select animal type" else petTypeState.value)
                }
                DropdownMenu(
                    expanded = showPetTypeDropdown,
                    onDismissRequest = { showPetTypeDropdown = false },
                ) {
                    PET_TYPES.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                petTypeState.value = type
                                viewModel.updatePetType(type)
                                showPetTypeDropdown = false
                                breedState.value = ""
                                viewModel.updateBreed("")
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Breed Dropdown
                Text(text = "Breed", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(6.dp))
                FilledTonalButton(
                    onClick = { showBreedDropdown = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = petTypeState.value.isNotBlank(),
                    shape = containerShape
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.happy_pet),
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (breedState.value.isBlank()) "Pick breed" else breedState.value)
                }
                DropdownMenu(
                    expanded = showBreedDropdown,
                    onDismissRequest = { showBreedDropdown = false },
                ) {
                    val breeds = when (petTypeState.value) {
                        "Dog" -> DOG_BREEDS
                        "Cat" -> CAT_BREEDS
                        "Bird" -> BIRD_BREEDS
                        else -> listOf("Other")
                    }
                    breeds.forEach { breed ->
                        DropdownMenuItem(
                            text = { Text(breed) },
                            onClick = {
                                breedState.value = breed
                                viewModel.updateBreed(breed)
                                showBreedDropdown = false
                            }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Weight +/- unit
                Text(text = "Weight", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = if (weightValueState.value == 0.0) "" else weightValueState.value.toString(),
                        onValueChange = { valueStr ->
                            val v = valueStr.toDoubleOrNull() ?: 0.0
                            weightValueState.value = v
                            viewModel.updateWeight(Weight(weightUnitState.value, v))
                        },
                        label = { Text("Weight") },
                        modifier = Modifier.weight(2f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(Res.drawable.happy_pet),
                                contentDescription = null
                            )
                        },
                        placeholder = { Text("e.g. 7.2") }
                    )
                    FilledTonalButton(
                        onClick = { showWeightUnitDropdown = true },
                        modifier = Modifier.weight(1f),
                        shape = containerShape
                    ) {
                        Text(weightUnitState.value)
                        Icon(
                            painter = painterResource(Res.drawable.loading_paw),
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showWeightUnitDropdown,
                        onDismissRequest = { showWeightUnitDropdown = false },
                    ) {
                        WEIGHT_UNITS.forEach { unitOpt ->
                            DropdownMenuItem(
                                text = { Text(unitOpt) },
                                onClick = {
                                    weightUnitState.value = unitOpt
                                    viewModel.updateWeight(Weight(unitOpt, weightValueState.value))
                                    showWeightUnitDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(text = "Size", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(6.dp))
                FilledTonalButton(
                    onClick = { showSizeDropdown = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = containerShape
                ) {
                    Text(if (sizeState.value.isBlank()) "Choose size" else sizeState.value)
                    Icon(
                        painter = painterResource(Res.drawable.loading_paw),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
                DropdownMenu(
                    expanded = showSizeDropdown,
                    onDismissRequest = { showSizeDropdown = false },
                ) {
                    SIZE_OPTIONS.forEach { size ->
                        DropdownMenuItem(
                            text = { Text(size) },
                            onClick = {
                                sizeState.value = size
                                viewModel.updateSize(size)
                                showSizeDropdown = false
                            }
                        )
                    }
                }

                Spacer(Modifier.height(18.dp))

                OutlinedTextField(
                    value = markingsState.value,
                    onValueChange = { markings ->
                        markingsState.value = markings
                        viewModel.updateColorMarkings(markings)
                    },
                    label = { Text("Color & Markings") },
                    placeholder = { Text("e.g., Brown with white patches") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.happy_pet),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdentificationPage(viewModel: AddPetViewModel) {
    val pet = viewModel.uiState.value.pet

    var showMicrochipLocationDropdown by remember { mutableStateOf(false) }
    var showClipLocationDropdown by remember { mutableStateOf(false) }
    var newAllergy by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Identification",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        var microchipNumber by remember { mutableStateOf("") }
        // Microchip number
        OutlinedTextField(
            value = microchipNumber,
            onValueChange = { number -> viewModel.updateMicrochipNumber(number) },
            label = { Text("Microchip Number") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.happy_pet),
                    contentDescription = null
                )
            },
            placeholder = { Text("e.g., MC-123456789") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Microchip location
        ExposedDropdownMenuBox(
            expanded = showMicrochipLocationDropdown,
            onExpandedChange = { showMicrochipLocationDropdown = !showMicrochipLocationDropdown }
        ) {
            var microchipLocation by remember { mutableStateOf("") }
            OutlinedTextField(
                value = microchipLocation,
                onValueChange = { viewModel.updateMicrochipLocation(it) },
                readOnly = true,
                label = { Text("Microchip Location") },
                modifier = Modifier
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.happy_pet),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = showMicrochipLocationDropdown)
                }
            )
            ExposedDropdownMenu(
                expanded = showMicrochipLocationDropdown,
                onDismissRequest = { showMicrochipLocationDropdown = false }
            ) {
                MICROCHIP_LOCATIONS.forEach { location: String ->
                    DropdownMenuItem(
                        text = { Text(location) },
                        onClick = {
                            viewModel.updateMicrochipLocation(location)
                            showMicrochipLocationDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Clip location
        ExposedDropdownMenuBox(
            expanded = showClipLocationDropdown,
            onExpandedChange = { showClipLocationDropdown = !showClipLocationDropdown }
        ) {

            var clipLocation by remember { mutableStateOf("") }
            OutlinedTextField(
                value = clipLocation,
                onValueChange = { viewModel.updateClipLocation(it) },
                readOnly = true,
                label = { Text("Clip Location") },
                modifier = Modifier
                    .fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.happy_pet),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = showClipLocationDropdown)
                }
            )
            ExposedDropdownMenu(
                expanded = showClipLocationDropdown,
                onDismissRequest = { showClipLocationDropdown = false }
            ) {
                CLIP_LOCATIONS.forEach { location: String ->
                    DropdownMenuItem(
                        text = { Text(location) },
                        onClick = {
                            viewModel.updateClipLocation(location)
                            showClipLocationDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Neutered/Spayed toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Neutered/Spayed",
                style = MaterialTheme.typography.bodyLarge
            )
            var checked by remember { mutableStateOf(false) }
            Switch(
                checked = checked,
                onCheckedChange = { checked: Boolean -> viewModel.updateNeuteredSpayed(checked) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Allergies section
        Text(
            text = "Allergies",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Add allergy input
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newAllergy,
                onValueChange = { allergy -> newAllergy = allergy },
                label = { Text("Add allergy") },
                modifier = Modifier.weight(1f),
                placeholder = { Text("e.g., Fish, Chicken") }
            )
            FilledTonalButton(
                onClick = {
//                    if (newAllergy.isNotBlank()) {
//                        val updatedAllergies = pet.identifiers.allergies + newAllergy.trim()
//                        viewModel.updateAllergies(updatedAllergies)
//                        newAllergy = ""
//                    }
                },
                enabled = newAllergy.isNotBlank()
            ) {
                Icon(
                    painter = painterResource(Res.drawable.happy_pet),
                    contentDescription = "Add"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Allergies chips
        val allergies = pet?.identifiers?.allergies ?: emptyList<String>()
        if (allergies.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = allergies) { allergy ->
                    InputChip(
                        onClick = { },
                        label = { Text(text = allergy) },
                        selected = false,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(Res.drawable.happy_pet),
                                contentDescription = "Remove $allergy",
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        val updatedAllergies = allergies.filter { it != allergy }
                                        viewModel.updateAllergies(updatedAllergies)
                                    }
                            )
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
//                    datePickerState.selectedDateMillis?.let { millis ->
//                        // Convert millis to date string format (YYYY-MM-DD)
//                        val date = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
//                            .format(java.util.Date(millis))
//                        onDateSelected(date)
//                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

// Validation function for page completion
fun isPageValid(page: Int, pet: Pet): Boolean {
    return when (page) {
        0 -> pet.name.isNotBlank() && pet.gender.isNotBlank() && pet.dateOfBirth.isNotBlank()
        1 -> pet.petType.isNotBlank() && pet.breed.isNotBlank() && pet.weight.value > 0
        2 -> true // All fields are optional on identification page
        else -> false
    }
}

@Preview
@Composable
fun AddPetScreenPreview() {
    // --- Provide a mock PetViewModel and Pet for preview ---

    TailyTheme {
        val dummyRepo = object : PetRepository {
            override suspend fun getPets() = flowOf(emptyList<Pet>())
            override suspend fun getPetsByUserId() =
                flowOf(emptyList<Pet>())
        }
        val pet = MockData.mockPets.first()
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {


            AddPetScreen(
                navHost = rememberNavController(),
                topPadding = 0.dp,
                viewModel = koinViewModel<AddPetViewModel>()
            )

        }
    }

}
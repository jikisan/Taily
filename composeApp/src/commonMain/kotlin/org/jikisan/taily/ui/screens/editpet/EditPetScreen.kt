package org.jikisan.taily.ui.screens.editpet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.vidspark.androidapp.ui.theme.TailyTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.enum.GenderType
import org.jikisan.taily.ui.common.EmptyScreen
import org.jikisan.taily.ui.common.ErrorScreen
import org.jikisan.taily.ui.common.LoadingScreen
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.PetConstants.FRACTIONAL_PARTS_1_DECIMAL
import org.jikisan.taily.ui.screens.addpet.PetConstants.PET_BREEDS
import org.jikisan.taily.ui.screens.addpet.PetConstants.PET_TYPES
import org.jikisan.taily.ui.screens.addpet.PetConstants.WEIGHT_UNITS
import org.jikisan.taily.ui.screens.addpet.addpetcontent.CarouselPicker
import org.jikisan.taily.ui.screens.addpet.addpetcontent.CustomDatePickerDialog
import org.jikisan.taily.util.DateUtils.convertToISO_MPP
import org.jikisan.taily.util.DateUtils.formatDateForDisplay
import org.jikisan.taily.util.DateUtils.formatDateToString
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.add_photo_alternate_24px
import taily.composeapp.generated.resources.arrow_back_ios_new_24px
import taily.composeapp.generated.resources.arrow_drop_down_24px
import taily.composeapp.generated.resources.calendar_month_24px
import taily.composeapp.generated.resources.female_24px
import taily.composeapp.generated.resources.male_24px
import taily.composeapp.generated.resources.sad_cat
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPetScreen(
    petId: String,
    navHost: NavHostController,
    topPadding: Dp,
    viewModel: EditPetViewModel = koinViewModel<EditPetViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var isLimitReached by remember { mutableStateOf(false) }

    var selectedGender by remember { mutableStateOf(GenderType.Female ) }

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var dob by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf<String?>(null) }
    var isDateError by remember { mutableStateOf(false) }

    var showSpeciesPicker by remember { mutableStateOf(false) }
    var showCustomSpeciesField by remember { mutableStateOf(false) }
    var selectedSpecies by remember { mutableStateOf("") }
    var customSpecies by remember { mutableStateOf("") }

    var showBreedPicker by remember { mutableStateOf(false) }
    var showCustomBreedField by remember { mutableStateOf(false) }
    var selectedBreed by remember { mutableStateOf("") }
    var customBreed by remember { mutableStateOf("") }

    val sheetState = rememberModalBottomSheetState()
    val weightRange = remember { (0..200).map { it.toString() } }
    val apiWeight = uiState.pet?.weight?.value ?: 0.0
    val apiUnit = uiState.pet?.weight?.unit ?: WEIGHT_UNITS.first()
    val initialWhole = apiWeight.toInt()
    val initialFraction = ((apiWeight - initialWhole) * 10).roundToInt() / 10.0
    val closestFraction = FRACTIONAL_PARTS_1_DECIMAL.minByOrNull {
        kotlin.math.abs(it.toFloat() - initialFraction)
    } ?: FRACTIONAL_PARTS_1_DECIMAL.first()
    var showWeightPicker by remember { mutableStateOf(false) }
    val selectedWeight = remember { mutableStateOf(initialWhole) }
    val selectedFraction = remember { mutableStateOf(closestFraction) }
    val selectedUnit = remember { mutableStateOf(apiUnit) }
    val finalWeight = remember { derivedStateOf { selectedWeight.value + selectedFraction.value.toDouble() } }
    val weight = remember { derivedStateOf { "${finalWeight.value} ${selectedUnit.value}" } }





    LaunchedEffect(Unit) {
        viewModel.loadPetDetails(petId)
    }

    LaunchedEffect(
        finalWeight.value,
        selectedWeight.value,
        selectedFraction.value,
        selectedUnit.value,
    ) {
        viewModel.updateWeight(
            Weight(
                unit = selectedUnit.value,
                value = finalWeight.value
            )
        )
    }

    LaunchedEffect(uiState.pet?.weight) {
        uiState.pet?.weight?.let { weight ->
            val whole = weight.value.toInt()
            val fraction = ((weight.value - whole) * 10).roundToInt() / 10.0
            val closestFraction = FRACTIONAL_PARTS_1_DECIMAL.minByOrNull {
                kotlin.math.abs(it.toDouble() - fraction)
            } ?: FRACTIONAL_PARTS_1_DECIMAL.first()

            selectedWeight.value = whole
            selectedFraction.value = closestFraction
            selectedUnit.value = weight.unit
        }
    }

    LaunchedEffect(uiState.pet?.gender) {
        uiState.pet?.gender?.let { gender ->
            selectedGender = if (gender == GenderType.Female.name) GenderType.Female else GenderType.Male
        }
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
                    .padding(top = topPadding, bottom = 16.dp)
                    .verticalScroll(scrollState),
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
                        modifier = Modifier.clickable(
                            onClick = { }
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

//                        Icon(
//                            imageVector = Icons.Default.Save,
//                            contentDescription = "",
//                            tint = MaterialTheme.colorScheme.onSurface,
//                            modifier = Modifier.padding(8.dp)
//                        )

                        Text(
                            text = "Save",
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp)
                        )
                    }


                }

                // Profile picture
                uiState.pet?.let { pet ->

                    Box(
                        modifier = Modifier.height(150.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {

                        Box {

                            AsyncImage(
                                model = pet.photo.url,
                                contentDescription = "Pet Profile Picture",
                                modifier = Modifier
                                    .clip(shape = CircleShape)
                                    .aspectRatio(1f / 1f),
                                contentScale = ContentScale.Crop,
                            )

                            Box(
                                modifier = Modifier
                                    .clip(shape = CircleShape)
                                    .padding(4.dp)
                                    .background(Color.White)
                                    .align(Alignment.BottomEnd),
                                contentAlignment = Alignment.Center
                            ) {


                                Icon(
                                    painter = painterResource(Res.drawable.add_photo_alternate_24px),
                                    contentDescription = "Edit Pet Profile Picture",
                                    modifier = Modifier
                                        .size(40.dp),
                                    tint = MaterialTheme.colorScheme.primary

                                )
                            }

                        }

                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        // PET NAME
                        InputTextField(
                            value = pet.name,
                            label = "Pet Name",
                            maxLength = 20,
                            onValueChange = { text: String ->
                                isLimitReached = text.length >= 20
                                viewModel.updateName(text)
                            },
                            isError = isLimitReached,
                            errorMessage = "Maximum character limit reached",
                        )

                        // PET GENDER
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                        ) {

                            Text(
                                text = "Pet Gender",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Row(
                                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,

                                ) {
                                GenderCard(
                                    drawable = Res.drawable.male_24px,
                                    text = "Male",
                                    isSelected = selectedGender == GenderType.Male,
                                    onClick = {
                                        selectedGender = GenderType.Male
                                        viewModel.updateGender(selectedGender)
                                    },
                                )
                                GenderCard(
                                    drawable = Res.drawable.female_24px,
                                    text = "Female",
                                    isSelected = selectedGender == GenderType.Female,
                                    onClick = {
                                        selectedGender = GenderType.Female
                                        viewModel.updateGender(selectedGender)
                                    },
                                )
                            }
                        }

                        // PET DOB
                        Column {
                            InputTextField(
                                value = formatDateForDisplay(pet.dateOfBirth),
                                onValueChange = { /* Read-only field */ },
                                label = "Pet Date of Birth",
                                readOnly = true,
                                errorMessage = dateError ?: "",
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(
                                            painter = painterResource(Res.drawable.calendar_month_24px),
                                            contentDescription = "Select date",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                },
                                modifier = Modifier.padding(horizontal = 16.dp),
                                showCharacterCounter = false,
                                isError = isDateError
                            )

                            if (showDatePicker) {
                                CustomDatePickerDialog(
                                    datePickerState = datePickerState,
                                    onDateSelected = { selectedDateMillis ->
                                        selectedDateMillis?.let { millis ->
                                            val selectedDate = Instant.fromEpochMilliseconds(millis)
                                                .toLocalDateTime(TimeZone.currentSystemDefault()).date
                                            val currentDate = Clock.System.now()
                                                .toLocalDateTime(TimeZone.currentSystemDefault()).date

                                            when {
                                                selectedDate > currentDate -> {
                                                    dateError = "Date cannot be in the future"
                                                    isDateError = true
                                                }

                                                else -> {
                                                    val maxAgeDate =
                                                        currentDate.minus(50, DateTimeUnit.YEAR)

                                                    if (selectedDate < maxAgeDate) {
                                                        dateError = "Please enter a reasonable date"
                                                        isDateError = true
                                                    } else {
                                                        dateError = null
                                                        dob = formatDateToString(selectedDate)
                                                        isDateError = false
                                                        viewModel.updateDateOfBirth(
                                                            convertToISO_MPP(
                                                                dob
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        showDatePicker = false
                                    },
                                    onDismiss = { showDatePicker = false }
                                )
                            }
                        }

                        // PET SPECIES
                        Column {
                            selectedSpecies = pet.petType
                            InputTextField(
                                value = if (showCustomSpeciesField) "Other" else selectedSpecies,
                                label = "Pet Species",
                                onValueChange = { },
                                placeholder = "e.g. Dog",
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = {
                                        showSpeciesPicker = !showSpeciesPicker
                                    }) {
                                        Icon(
                                            painter = painterResource(Res.drawable.arrow_drop_down_24px),
                                            contentDescription = "Select species",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = showSpeciesPicker,
                                        onDismissRequest = { showSpeciesPicker = false },
                                        modifier = Modifier.width(200.dp),
                                        shape = RoundedCornerShape(10),
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        border = BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.primary
                                        ),
                                    ) {
                                        PET_TYPES.forEach { petType ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = petType,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        textAlign = TextAlign.Center,
                                                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                                    )
                                                },
                                                onClick = {
                                                    showSpeciesPicker = false
                                                    selectedSpecies = petType
                                                    if (petType == "Other") {
                                                        showCustomSpeciesField = true
                                                    } else {
                                                        showCustomSpeciesField = false
                                                        customSpecies = ""
                                                        viewModel.updatePetType(petType)
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.padding(horizontal = 16.dp),
                                showCharacterCounter = false,
                            )

                            if (showCustomSpeciesField) {
                                Spacer(modifier = Modifier.height(4.dp))

                                Box(
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                ) {
                                    ThemeOutlineTextField(
                                        value = customSpecies,
                                        onValueChange = { newValue ->
                                            customSpecies = newValue
                                            viewModel.updatePetType(newValue)
                                        },
                                        placeholder = "Enter ${pet?.name ?: "pet"}'s species",
                                        readOnly = false,
                                        showCharacterCounter = false,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }


                            }
                        }

                        // PET BREED
                        Column {
                            val hasBreeds = PET_BREEDS.containsKey(uiState.pet?.petType)

                            if (hasBreeds) {

                                selectedBreed = pet.breed
                                InputTextField(
                                    value = if (showCustomBreedField) "Other" else selectedBreed,
                                    label = "Pet Breed",
                                    onValueChange = { },
                                    placeholder = "Enter ${pet?.name ?: "pet"}'s breed",
                                    readOnly = true,
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            showBreedPicker = !showBreedPicker
                                        }) {
                                            Icon(
                                                painter = painterResource(Res.drawable.arrow_drop_down_24px),
                                                contentDescription = "Select breed",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }

                                        DropdownMenu(
                                            expanded = showBreedPicker,
                                            onDismissRequest = { showBreedPicker = false },
                                            modifier = Modifier.width(200.dp),
                                            shape = RoundedCornerShape(10),
                                            containerColor = MaterialTheme.colorScheme.surface,
                                            border = BorderStroke(
                                                1.dp,
                                                MaterialTheme.colorScheme.primary
                                            ),
                                        ) {
                                            pet?.petType?.let { petType ->
                                                PET_BREEDS[petType]?.forEach {
                                                    DropdownMenuItem(
                                                        text = {
                                                            Text(
                                                                text = it,
                                                                modifier = Modifier.fillMaxWidth(),
                                                                textAlign = TextAlign.Center,
                                                                fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                                            )
                                                        },
                                                        onClick = {
                                                            showBreedPicker = false

                                                            if (it.equals(
                                                                    "Other",
                                                                    ignoreCase = true
                                                                ) || petType.equals(
                                                                    "Other",
                                                                    ignoreCase = true
                                                                )
                                                            ) {
                                                                showCustomBreedField = true
                                                                // Don't update viewModel yet, wait for custom input
                                                            } else {
                                                                showCustomBreedField = false
                                                                customBreed = ""
                                                                viewModel.updateBreed(it)
                                                            }
                                                        },
                                                        modifier = Modifier.fillMaxWidth(),

                                                        )
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    showCharacterCounter = false,
                                )

                                if (showCustomBreedField) {

                                    Spacer(modifier = Modifier.height(16.dp))

                                    ThemeOutlineTextField(
                                        value = customBreed,
                                        onValueChange = { newValue ->
                                            customBreed = newValue
                                            viewModel.updatePetType(newValue)
                                        },
                                        placeholder = "Enter ${pet?.name ?: "pet"}'s breed",
                                        readOnly = false,
                                        showCharacterCounter = false,
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                    )

                                }
                            } else {
                                viewModel.updateBreed("")
                            }

                        }

                        uiState.pet?.weight

                        //PET WEIGHT
                        Column {
                            InputTextField(
                                value = weight.value,
                                label = "Pet Weight",
                                onValueChange = {}, // Read-only
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showWeightPicker = true }) {
                                        Icon(
                                            painter = painterResource(Res.drawable.arrow_drop_down_24px),
                                            contentDescription = "Select weight",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                },
                                modifier = Modifier.padding(horizontal = 16.dp),
                                showCharacterCounter = false,
                            )
                            if (showWeightPicker) {
                                ModalBottomSheet(
                                    modifier = Modifier.fillMaxWidth(),
                                    sheetState = sheetState,
                                    onDismissRequest = { showWeightPicker = false },
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                    ) {
                                        // Whole number picker
                                        CarouselPicker(
                                            items = weightRange,
                                            selectedItem = selectedWeight.value.toString(), // ✅ API value
                                            onItemSelected = { selectedWeight.value = it.toInt() },
                                            modifier = Modifier.weight(1f)
                                        )

                                        // Fraction picker
                                        CarouselPicker(
                                            items = FRACTIONAL_PARTS_1_DECIMAL,
                                            selectedItem = selectedFraction.value, // ✅ API fraction
                                            onItemSelected = { selectedFraction.value = it },
                                            modifier = Modifier.weight(1f)
                                        )

                                        // Unit picker
                                        CarouselPicker(
                                            items = WEIGHT_UNITS,
                                            selectedItem = selectedUnit.value, // ✅ API unit
                                            onItemSelected = { selectedUnit.value = it },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }

                        //PET IDENTIFIERS

                    }


                }

            }
        }
    }
}


@Composable
fun InputTextField(
    label: String,
    value: String,
    placeholder: String? = "",
    onValueChange: (String) -> Unit,
    maxLength: Int = 20,
    modifier: Modifier = Modifier.padding(horizontal = 16.dp),
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    showCharacterCounter: Boolean = true,
    errorMessage: String? = "",
    isError: Boolean = false
) {

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )

        ThemeOutlineTextField(
            value = value,
            placeholder = placeholder!!,
            onValueChange = { newValue ->
                onValueChange(newValue)
            },
            isError = isError,
            trailingIcon = trailingIcon,
            readOnly = readOnly,
            maxLength = maxLength,
            showCharacterCounter = showCharacterCounter,
            errorMessage = errorMessage!!,
        )

//        if (isError) {
//            Text(
//                text = "Max $maxLength characters allowed",
//                style = MaterialTheme.typography.labelSmall,
//                color = MaterialTheme.colorScheme.error,
//            )
//        }
    }

}

@Composable
fun GenderCard(
    drawable: DrawableResource,
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val borderColor = MaterialTheme.colorScheme.primary
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = text,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = borderColor
            )
        }
    }
}


@Preview
@Composable
fun EditPetScreenPreview() {

    TailyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            EditPetScreen(petId = "", navHost = rememberNavController(), topPadding = 0.dp)
        }
    }
}
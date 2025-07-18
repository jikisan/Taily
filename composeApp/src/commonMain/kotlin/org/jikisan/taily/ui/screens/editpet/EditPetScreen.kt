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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import org.jikisan.taily.domain.model.enum.GenderType
import org.jikisan.taily.ui.common.EmptyScreen
import org.jikisan.taily.ui.common.ErrorScreen
import org.jikisan.taily.ui.common.LoadingScreen
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.PetConstants.PET_TYPES
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
    var showSpeciesPicker by remember { mutableStateOf(false) }
    var showCustomSpeciesField by remember { mutableStateOf(false) }
    var selectedSpecies by remember { mutableStateOf("") }
    var customSpecies by remember { mutableStateOf("") }


    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var dob by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf<String?>(null) }
    var isDateError by remember { mutableStateOf(false) }


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
                            var selectedGender by remember {
                                mutableStateOf(
                                    if (pet?.gender == GenderType.Female.name) GenderType.Female
                                    else GenderType.Male
                                )
                            }
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
                                                        viewModel.updateDateOfBirth(convertToISO_MPP(dob))
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
                                    IconButton(onClick = { showSpeciesPicker = !showSpeciesPicker }) {
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
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
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
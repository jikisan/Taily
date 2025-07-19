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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.benasher44.uuid.uuid4
import com.vidspark.androidapp.ui.theme.TailyTheme
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import network.chaintech.cmpimagepickncrop.CMPImagePickNCropDialog
import network.chaintech.cmpimagepickncrop.imagecompress.compressImage
import network.chaintech.cmpimagepickncrop.imagecropper.ImageAspectRatio
import network.chaintech.cmpimagepickncrop.imagecropper.rememberImageCropper
import network.chaintech.cmpimagepickncrop.utils.ImageFileFormat
import network.chaintech.cmpimagepickncrop.utils.ImagePickerDialogStyle
import network.chaintech.cmpimagepickncrop.utils.toByteArray
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.cmpecommerceapp.util.ApiRoutes.TAG
import org.jikisan.taily.domain.model.Photo
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.enum.GenderType
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.Identifiers
import org.jikisan.taily.ui.common.EmptyScreen
import org.jikisan.taily.ui.common.ErrorScreen
import org.jikisan.taily.ui.common.LoadingScreen
import org.jikisan.taily.ui.common.UploadingScreen
import org.jikisan.taily.ui.components.AnimatedGradientSnackbarHost
import org.jikisan.taily.ui.components.DropdownMenuField
import org.jikisan.taily.ui.components.SnackbarDuration
import org.jikisan.taily.ui.components.SnackbarType
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.PetConstants.CLIP_LOCATIONS
import org.jikisan.taily.ui.screens.addpet.PetConstants.FRACTIONAL_PARTS_1_DECIMAL
import org.jikisan.taily.ui.screens.addpet.PetConstants.MICROCHIP_LOCATIONS
import org.jikisan.taily.ui.screens.addpet.PetConstants.PET_BREEDS
import org.jikisan.taily.ui.screens.addpet.PetConstants.PET_TYPES
import org.jikisan.taily.ui.screens.addpet.PetConstants.SIZE_OPTIONS
import org.jikisan.taily.ui.screens.addpet.PetConstants.WEIGHT_UNITS
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AllergyList
import org.jikisan.taily.ui.screens.addpet.addpetcontent.CarouselPicker
import org.jikisan.taily.ui.screens.addpet.addpetcontent.CustomDatePickerDialog
import org.jikisan.taily.util.DateUtils.convertToISO_MPP
import org.jikisan.taily.util.DateUtils.formatDateForDisplay
import org.jikisan.taily.util.DateUtils.formatDateToString
import org.jikisan.taily.util.readFileAsBytes
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val originalPet = remember { mutableStateOf<Pet?>(null) }

    var isLimitReached by remember { mutableStateOf(false) }

    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var openImagePicker by remember { mutableStateOf(false) }
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }

    val imageCropper = rememberImageCropper()
    val cameraScope = rememberCoroutineScope()


    var selectedGender by remember { mutableStateOf(GenderType.Male) }

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

    var hasMicrochip by remember { mutableStateOf(uiState.pet?.identifiers?.microchipNumber != "") }
    var microchipNumber by remember {
        mutableStateOf(
            uiState.pet?.identifiers?.microchipNumber ?: ""
        )
    }
    var microchipLocation by remember {
        mutableStateOf(
            uiState.pet?.identifiers?.microchipLocation ?: ""
        )
    }
    var clipLocation by remember { mutableStateOf(uiState.pet?.identifiers?.clipLocation ?: "") }
    var size by remember { mutableStateOf(uiState.pet?.identifiers?.size ?: "") }
    var colorMarkings by remember { mutableStateOf(uiState.pet?.identifiers?.colorMarkings ?: "") }
    var isNeuteredOrSpayed by remember {
        mutableStateOf(
            uiState.pet?.identifiers?.isNeuteredOrSpayed ?: false
        )
    }
    val allergies = remember {
        mutableStateListOf<String>().apply {
            addAll(
                uiState.pet?.identifiers?.allergies ?: emptyList()
            )
        }
    }
    var newAllergy by remember { mutableStateOf("") }

    var showSubmitDialog by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.loadPetDetails(petId)
    }

    LaunchedEffect(uiState.pet) {
        uiState.pet?.let { pet ->
            if (originalPet.value == null) {
                originalPet.value = pet.copy()
            }

            // Gender
            selectedGender =
                if (pet.gender == GenderType.Female.name) GenderType.Female else GenderType.Male

            // Weight
            pet.weight?.let { weight ->
                val whole = weight.value.toInt()
                val fraction = ((weight.value - whole) * 10).roundToInt() / 10.0
                val closestFraction = FRACTIONAL_PARTS_1_DECIMAL.minByOrNull {
                    kotlin.math.abs(it.toDouble() - fraction)
                } ?: FRACTIONAL_PARTS_1_DECIMAL.first()

                selectedWeight.value = whole
                selectedFraction.value = closestFraction
                selectedUnit.value = weight.unit
            }

            // Image
            pet.imageBitmap?.let { selectedImage = it }

            // Identifiers
            pet.identifiers?.let { identifiers ->
                hasMicrochip = identifiers.microchipNumber?.isNotBlank() ?: false
                if (hasMicrochip) {
                    microchipNumber = identifiers.microchipNumber ?: ""
                    microchipLocation = identifiers.microchipLocation ?: ""
                }
                clipLocation = identifiers.clipLocation ?: ""
                size = identifiers.size ?: ""
                colorMarkings = identifiers.colorMarkings ?: ""
                isNeuteredOrSpayed = identifiers.isNeuteredOrSpayed ?: false
                allergies.clear()
                allergies.addAll(identifiers.allergies)
            }
        }
    }

    LaunchedEffect(
        finalWeight.value,
        selectedWeight.value,
        selectedFraction.value,
        selectedUnit.value,
    ) {
        delay(300)
        viewModel.updateWeight(
            Weight(
                unit = selectedUnit.value,
                value = finalWeight.value
            )
        )
    }

    LaunchedEffect(
        hasMicrochip,
        microchipNumber,
        microchipLocation,
        clipLocation,
        size,
        colorMarkings,
        isNeuteredOrSpayed,
        allergies.toList()
    ) {
        delay(300)
        viewModel.updateIdentifiers(
            Identifiers(
                microchipNumber = if (hasMicrochip) microchipNumber else "",
                microchipLocation = if (hasMicrochip) microchipLocation else "",
                clipLocation = clipLocation,
                size = size,
                colorMarkings = colorMarkings,
                isNeuteredOrSpayed = isNeuteredOrSpayed,
                allergies = allergies.toList()
            )
        )
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


                    TextButton(
                        onClick = { showSubmitDialog = true },
                    ) {
                        Text(
                            text = "Save",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }


                }


                uiState.pet?.let { pet ->

                    // Profile picture
                    Box(
                        modifier = Modifier.height(150.dp).fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {

                        Box {

                            CMPImagePickNCropDialog(
                                imageCropper = imageCropper,
                                openImagePicker = openImagePicker,
                                defaultAspectRatio = ImageAspectRatio(16, 9),
                                showCameraOption = false,
                                imagePickerDialogStyle = ImagePickerDialogStyle(
                                    title = "Choose from option",
                                    txtCamera = "From Camera",
                                    txtGallery = "From Gallery",
                                    txtCameraColor = Color.DarkGray,
                                    txtGalleryColor = Color.DarkGray,
                                    cameraIconTint = Color.DarkGray,
                                    galleryIconTint = Color.DarkGray,
                                    backgroundColor = Color.White
                                ),
                                autoZoom = false,
                                imagePickerDialogHandler = { openImagePicker = it },
                                selectedImageCallback = { imageBitmap ->

                                    selectedImage = imageBitmap

                                    selectedImage?.let { image ->
                                        viewModel.updatePhotoImageBitmap(image)
                                    }
//
                                    imageByteArray = imageBitmap.toByteArray(
                                        format = ImageFileFormat.JPEG,
                                        quality = 0.6f
                                    )

                                    // For server upload
                                    imageByteArray.let { byteArray ->
                                        val fileName = "${uuid4()}.jpg"
                                        viewModel.updatePhoto(Photo(url = "", name = fileName))
                                        viewModel.updatePetPhotoByteArray(byteArray!!)
                                    }
//                    }
//


                                },
                                selectedImageFileCallback = { sharedImage ->
                                    cameraScope.launch {

//                                        val compressedFilePath = compressImage(
//                                            sharedImage = sharedImage,
//                                            targetFileSize = 200 * 1024,
//                                            fileName = "pet_profile_photo"
//                                        )
//
//                                        compressedFilePath?.let { path ->
//                                            val compressedByteArray = readFileAsBytes(path)
//                                            compressedByteArray?.let { byteArray ->
//                                                // For server upload
//                                                viewModel.updatePetPhotoByteArray(byteArray)
//
//
//                                            }
//                                        }

                                    }

                                }
                            )

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
                                if (selectedImage == null) {
                                    AsyncImage(
                                        model = pet.photo.url,
                                        contentDescription = "Pet Profile Picture",
                                        modifier = Modifier
                                            .clip(shape = CircleShape)
                                            .aspectRatio(1f / 1f),
                                        contentScale = ContentScale.Crop,
                                    )
                                } else {
                                    Image(
                                        bitmap = selectedImage!!,
                                        contentDescription = "Pet photo",
                                        modifier = Modifier
                                            .clip(shape = CircleShape)
                                            .aspectRatio(1f / 1f),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }






                            Box(
                                modifier = Modifier
                                    .clip(shape = CircleShape)
                                    .background(Color.White)
                                    .align(Alignment.BottomEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = { openImagePicker = true },
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.add_photo_alternate_24px),
                                        contentDescription = "Edit Pet Profile Picture",
                                        modifier = Modifier
                                            .size(24.dp),
                                        tint = MaterialTheme.colorScheme.primary

                                    )
                                }
                            }

                        }

                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth().padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        //Pet Profile
                        // Pet name, gender, date of birth
                        Text(
                            text = "Pet Profile",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.semantics { heading() }
                                .padding(start = 16.dp, top = 16.dp)
                        )
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
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
                                                    val selectedDate =
                                                        Instant.fromEpochMilliseconds(millis)
                                                            .toLocalDateTime(TimeZone.currentSystemDefault()).date
                                                    val currentDate = Clock.System.now()
                                                        .toLocalDateTime(TimeZone.currentSystemDefault()).date

                                                    when {
                                                        selectedDate > currentDate -> {
                                                            dateError =
                                                                "Date cannot be in the future"
                                                            isDateError = true
                                                        }

                                                        else -> {
                                                            val maxAgeDate =
                                                                currentDate.minus(
                                                                    50,
                                                                    DateTimeUnit.YEAR
                                                                )

                                                            if (selectedDate < maxAgeDate) {
                                                                dateError =
                                                                    "Please enter a reasonable date"
                                                                isDateError = true
                                                            } else {
                                                                dateError = null
                                                                dob =
                                                                    formatDateToString(selectedDate)
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
                            }


                        }

                        //Pet Details
                        //Pet species, breed, weight
                        Text(
                            text = "Pet Details",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.semantics { heading() }
                                .padding(start = 16.dp, top = 16.dp)
                        )
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            ) {
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
                                                    onItemSelected = {
                                                        selectedWeight.value = it.toInt()
                                                    },
                                                    modifier = Modifier.weight(1f)
                                                )

                                                // Fraction picker
                                                CarouselPicker(
                                                    items = FRACTIONAL_PARTS_1_DECIMAL,
                                                    selectedItem = selectedFraction.value, // ✅ API fraction
                                                    onItemSelected = {
                                                        selectedFraction.value = it
                                                    },
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
                            }


                        }


                        //PET IDENTIFIERS
                        // Microchip number and location
                        Text(
                            text = "Microchip Information",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.semantics { heading() }
                                .padding(start = 16.dp, top = 16.dp)
                        )
                        // Microchip toggle
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp) // inner card padding
                            ) {
                                Text(
                                    text = "Does ${pet?.name ?: "your pet"} have a microchip?",
                                    modifier = Modifier.weight(1f)
                                )
                                Switch(
                                    checked = hasMicrochip,
                                    onCheckedChange = { hasMicrochip = it }
                                )
                            }

                            if (hasMicrochip) {
                                ThemeOutlineTextField(
                                    value = if (hasMicrochip) microchipNumber else "",
                                    placeholder = "Enter microchip number",
                                    onValueChange = { microchipNumber = it },
                                    maxLength = 20,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                )
                                DropdownMenuField(
                                    label = "Microchip Location",
                                    options = MICROCHIP_LOCATIONS,
                                    selectedOption = if (hasMicrochip) microchipLocation else "",
                                    onOptionSelected = { microchipLocation = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }

                        // Physical Identifiers
                        // Clip Location & Size & Color Markings
                        Text(
                            text = "Physical Identifiers",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.semantics { heading() }
                                .padding(start = 16.dp, top = 16.dp)
                        )

                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .background(color = MaterialTheme.colorScheme.background)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Clip Location
                                DropdownMenuField(
                                    label = "Clip Location",
                                    options = CLIP_LOCATIONS,
                                    selectedOption = clipLocation,
                                    onOptionSelected = { clipLocation = it },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                // Size
                                DropdownMenuField(
                                    label = "Size",
                                    options = SIZE_OPTIONS,
                                    selectedOption = size,
                                    onOptionSelected = { size = it },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                // Color Markings
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Color Markings",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    ThemeOutlineTextField(
                                        value = colorMarkings,
                                        placeholder = "Enter color markings",
                                        onValueChange = { colorMarkings = it },
                                        maxLength = 30,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                        }


                        // Health Information
                        //  Allergies & Neutered/Spayed toggle
                        Text(
                            text = "Health Information",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.semantics { heading() }
                                .padding(start = 16.dp, top = 16.dp)
                        )
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {

                            Column {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "Allergies",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        ThemeOutlineTextField(
                                            value = newAllergy,
                                            placeholder = "Add allergy",
                                            onValueChange = { newAllergy = it },
                                            maxLength = 20,
                                            modifier = Modifier.weight(1f),
                                            trailingIcon = {
                                                IconButton(
                                                    onClick = {
                                                        if (newAllergy.isNotBlank() && !allergies.contains(
                                                                newAllergy.trim()
                                                            )
                                                        ) {
                                                            allergies.add(newAllergy.trim())
                                                            newAllergy = ""
                                                        }
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Add,
                                                        contentDescription = "Add allergy",
                                                    )
                                                }
                                            }
                                        )

                                    }

                                    AllergyList(
                                        allergies = allergies,
                                        onRemove = { allergies.remove(it) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                                ) {
                                    Text(
                                        text = "Neutered/Spayed?",
                                        modifier = Modifier.weight(1f)
                                    )
                                    Switch(
                                        checked = isNeuteredOrSpayed,
                                        onCheckedChange = { isNeuteredOrSpayed = it }
                                    )
                                }
                            }

                        }


                    }


                }

            }
        }
    }

    if (showSubmitDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = { Text("Confirm Submission") },
            text = { Text("Are you sure all pet info are correct?") },
            confirmButton = {
                TextButton(onClick = {

                    viewModel.updateIsSubmitting(true)
                    showSubmitDialog = false
                    viewModel.submitPet()

                }) {
                    Text("Yes, Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog = false }) {
                    Text(text = "Cancel", color = Color.Gray)
                }
            }
        )

    }

    if (uiState.isSubmitting) {
        Dialog(
            onDismissRequest = { viewModel.updateIsSubmitting(false) },
            content = { UploadingScreen() }
        )
    }

    if (uiState.errorMessage != null) {
        AnimatedGradientSnackbarHost(
            message = uiState.errorMessage!!,
            type = SnackbarType.ERROR,
            onDismiss = { viewModel.updateErrorMessage(null) },
            duration = SnackbarDuration.LONG
        )
    }

    if (uiState.isSubmittingSuccess) {
        AnimatedGradientSnackbarHost(
            message = "Your pet has been added successfully!",
            type = SnackbarType.SUCCESS,
            onDismiss = {
                navHost.popBackStack()
            },
        )
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
                width = 1.dp,
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
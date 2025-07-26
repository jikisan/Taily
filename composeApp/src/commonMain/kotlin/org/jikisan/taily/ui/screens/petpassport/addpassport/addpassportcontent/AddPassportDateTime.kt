package org.jikisan.taily.ui.screens.petpassport.addpassport.addpassportcontent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.uuid4
import com.vidspark.androidapp.ui.theme.Blue
import com.vidspark.androidapp.ui.theme.OffBlue
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import network.chaintech.cmpimagepickncrop.CMPImagePickNCropDialog
import network.chaintech.cmpimagepickncrop.imagecropper.ImageAspectRatio
import network.chaintech.cmpimagepickncrop.imagecropper.rememberImageCropper
import network.chaintech.cmpimagepickncrop.utils.ImageFileFormat
import network.chaintech.cmpimagepickncrop.utils.ImagePickerDialogStyle
import network.chaintech.cmpimagepickncrop.utils.toByteArray
import org.jikisan.taily.domain.model.Photo
import org.jikisan.taily.model.pet.Schedule
import org.jikisan.taily.ui.components.CarouselPicker
import org.jikisan.taily.ui.components.ThemeClickableField
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.AddPetHeader
import org.jikisan.taily.ui.screens.addpet.PetConstants.FRACTIONAL_PARTS_1_DECIMAL
import org.jikisan.taily.ui.screens.addpet.PetConstants.WEIGHT_UNITS
import org.jikisan.taily.ui.screens.addpet.addpetcontent.CustomDatePickerDialog
import org.jikisan.taily.ui.screens.petpassport.addpassport.AddPassportSchedViewmodel
import org.jikisan.taily.ui.screens.petpassport.addpassport.addpassportcontent.PassportConstant.GIVEN_TYPES
import org.jikisan.taily.util.DateUtils.combineDateAndTimeToISO
import org.jikisan.taily.util.DateUtils.convertToISO_MPP
import org.jikisan.taily.util.DateUtils.formatDateForDisplay
import org.jikisan.taily.util.DateUtils.formatDateToString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPassportDateTime(viewModel: AddPassportSchedViewmodel, sched: Schedule) {

    var hasDatePassed by remember { mutableStateOf(false) }

    var isVetLimitReached by remember { mutableStateOf(false) }
    var isOtherGivenTypeLimitReached by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState()

    val datePickerState = rememberDatePickerState()
    var dateError by remember { mutableStateOf("") }
    var isDateError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var selectedDate by remember {
        mutableStateOf(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        )
    }
    var selectedHour by remember { mutableStateOf("07") }
    var selectedMinute by remember { mutableStateOf("30") }
    var selectedAmPm by remember { mutableStateOf("AM") }


    var showGivenTypes by remember { mutableStateOf(false) }
    var showOtherGivenTypeTextField by remember { mutableStateOf(false) }

    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var openImagePicker by remember { mutableStateOf(false) }
    var imageByteArray by remember { mutableStateOf<ByteArray?>(null) }

    val imageCropper = rememberImageCropper()

    var showWeightPicker by remember { mutableStateOf(false) }
    val weightRange = remember { (0..200).map { it.toString() } }
    val selectedWeight = remember { mutableStateOf(weightRange[7].toInt()) }
    val selectedFraction = remember { mutableStateOf(FRACTIONAL_PARTS_1_DECIMAL[3]) }
    val selectedUnit = remember { mutableStateOf(WEIGHT_UNITS[0]) }

    val finalWeight = remember {
        derivedStateOf {
            selectedWeight.value + selectedFraction.value.toDouble()
        }
    }

    val finalTime = remember {
        derivedStateOf {
            "${selectedHour}:${selectedMinute} ${selectedAmPm}"
        }
    }



    LaunchedEffect(Unit) {
        val now = Clock.System.now().plus(5, DateTimeUnit.HOUR)
        val localDateTime = now.toLocalDateTime(TimeZone.currentSystemDefault())
        selectedDate = localDateTime.date
        val hour24 = localDateTime.hour
        selectedHour = if (hour24 == 0 || hour24 == 12) "12" else (hour24 % 12).toString().padStart(2, '0')
        selectedMinute = localDateTime.minute.toString().padStart(2, '0')
        selectedAmPm = if (hour24 < 12) "AM" else "PM"
    }

    LaunchedEffect(
        selectedDate,
        selectedHour,
        selectedMinute,
        selectedAmPm
    ) {
        dateError = ""
        isDateError = false

        val combinedDateTimeStr = combineDateAndTimeToISO(
            date = selectedDate,
            timeStr = finalTime.value
        )

        // Current moment
        val nowInstant = Clock.System.now()

        // Convert combined string to Instant
        val combinedInstant = Instant.parse(combinedDateTimeStr)

        // Update full schedule datetime
        viewModel.updateSchedDateTime(newDateTime = combinedDateTimeStr)

        if (combinedInstant < nowInstant) {
            hasDatePassed = true
            viewModel.updateGivenDateTime(newDateTime = combinedDateTimeStr)
        } else {
            hasDatePassed = false
            viewModel.updateGivenDateTime("")
        }
    }


    LaunchedEffect(
        selectedWeight.value,
        selectedFraction.value,
        selectedUnit.value
    ) {
        viewModel.updateWeightValue(finalWeight.value)
        viewModel.updateWeightUnit(selectedUnit.value)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            AddPetHeader("Enter Schedule Date and Time")

            // Date and Time
            Row {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)

                ) {
                    ThemeClickableField (
                        value = formatDateForDisplay(sched.schedDateTime),
                        label = " \uD83D\uDCC5 Date",
                        placeholder = "e.g. May 15, 2020",
                        borderColor = Blue,
                        isError = isDateError,
                        errorMessage = dateError,
                        onClick = {
                            showDatePicker = true
                        }

                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    ThemeClickableField(
                        value = "${finalTime.value}",
                        label = "\uD83D\uDD52 Time",
                        placeholder = "e.g. 10:00 AM",
                        borderColor = Blue,
                        isError = false,
                        errorMessage = "",
                        onClick = { showTimePicker = true }

                    )
                }

            }
        }

        if (hasDatePassed) {

            Spacer(modifier = Modifier.height(16.dp))

            Column {
                Text(
                    text = "\uD83D\uDC3E Log Completed Appointment",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 4.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "This appointment is in the past — enter how your pet’s visit went!",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    textAlign = TextAlign.Center,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Given Type
                ThemeClickableField(
                    value = if (!showOtherGivenTypeTextField) {
                        "${sched.given.type}" ?: ""
                    } else {
                        "Other"
                    },
                    label = "\uD83D\uDC89 Given Type",
                    placeholder = "e.g. Injection",
                    borderColor = Blue,
                    isError = false,
                    errorMessage = "",
                    onClick = { showGivenTypes = true }
                )

                if (showOtherGivenTypeTextField) {
                    ThemeOutlineTextField(
                        value = sched.given.type ?: "",
                        label = "\uD83D\uDC89 Other Given Type",
                        placeholder = "Enter how the vaccine was given",
                        borderColor = Blue,
                        maxLength = 30,
                        onValueChange = { text ->
                            isOtherGivenTypeLimitReached = text.length >= 30
                            viewModel.updateGivenType(text)
                        },
                        isError = isOtherGivenTypeLimitReached,
                        errorMessage = "Maximum character limit reached",
                    )
                }

                DropdownMenu(
                    expanded = showGivenTypes,
                    onDismissRequest = { showGivenTypes = false },
                    modifier = Modifier.width(200.dp),
                    shape = RoundedCornerShape(10),
                    containerColor = OffBlue,
                    border = BorderStroke(1.dp, Blue),
                ) {
                    GIVEN_TYPES.forEach { givenType ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = givenType,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                )
                            },
                            onClick = {
                                if (givenType.equals("Other", ignoreCase = true)) {
                                    showOtherGivenTypeTextField = true
                                    viewModel.updateGivenType("")
                                } else {
                                    showOtherGivenTypeTextField = false
                                    viewModel.updateGivenType(givenType)

                                }
                                showGivenTypes = false
                            },
                            modifier = Modifier.fillMaxWidth(),

                            )
                    }
                }

                // Given Proof Photo
                ThemeClickableField(
                    value = sched.given.proofPhoto?.name ?: "",
                    label = "\uD83D\uDCF7 Photo",
                    placeholder = "Open Image Picker",
                    borderColor = Blue,
                    isError = false,
                    errorMessage = "",
                    onClick = { openImagePicker = true }
                )
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
                            viewModel.updateGivenProofPhoto(Photo(url = "", name = fileName))
                            viewModel.updatePetPhotoByteArray(byteArray!!)
                        }

                    },
                    selectedImageFileCallback = { sharedImage -> }


                )


                // Weight
                ThemeClickableField(
                    value = "${finalWeight.value} ${selectedUnit.value}" ?: "",
                    label = "\uD83E\uDDED Weight",
                    placeholder = "e.g. 2.5 kg",
                    borderColor = Blue,
                    isError = false,
                    errorMessage = "",
                    onClick = { showWeightPicker = true }
                )


                // Veterinarian
                ThemeOutlineTextField(
                    value = sched.vet ?: "",
                    label = "\uD83E\uDE7A Veterinarian",
                    placeholder = "e.g. Dr. Maria Santos",
                    borderColor = Blue,
                    maxLength = 30,
                    onValueChange = { text ->
                        isVetLimitReached = text.length >= 30
                        viewModel.updateVet(text)
                    },
                    isError = isVetLimitReached,
                    errorMessage = "Maximum character limit reached",
                )
            }

        }
    }

    if (showDatePicker) {
        CustomDatePickerDialog(
            datePickerState = datePickerState,
            onDateSelected = { selectedDateMillis ->
                selectedDateMillis?.let { millis ->
                    selectedDate =
                        Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showTimePicker) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            sheetState = sheetState,
            onDismissRequest = { showTimePicker = false },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                // Hour picker: 1 to 12
                CarouselPicker(
                    items = (1..12).map { it.toString().padStart(2, '0') },
                    selectedItem = selectedHour,
                    onItemSelected = { selectedHour = it },
                    modifier = Modifier.weight(1f)
                )

                // Minute picker: 00 to 59
                CarouselPicker(
                    items = (0..55 step 5).map { it.toString().padStart(2, '0') },
                    selectedItem = selectedMinute,
                    onItemSelected = { selectedMinute = it },
                    modifier = Modifier.weight(1f)
                )

                // AM/PM picker
                CarouselPicker(
                    items = listOf("AM", "PM"),
                    selectedItem = selectedAmPm,
                    onItemSelected = { selectedAmPm = it },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }

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
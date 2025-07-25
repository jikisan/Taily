package org.jikisan.taily.ui.screens.petpassport.addpassport

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vidspark.androidapp.ui.theme.Surface
import com.vidspark.androidapp.ui.theme.TailyTheme
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.validator.validate
import org.jikisan.taily.ui.common.UploadingScreen
import org.jikisan.taily.ui.components.ThemeClickableField
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.PetConstants.FRACTIONAL_PARTS_1_DECIMAL
import org.jikisan.taily.ui.screens.addpet.PetConstants.WEIGHT_UNITS
import org.jikisan.taily.ui.screens.addpet.addpetcontent.CarouselPicker
import org.jikisan.taily.ui.screens.addpet.addpetcontent.CustomDatePickerDialog
import org.jikisan.taily.util.DateUtils.convertToISO_MPP
import org.jikisan.taily.util.DateUtils.formatDateForDisplay
import org.jikisan.taily.util.DateUtils.formatDateToString
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.arrow_back_ios_new_24px

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPassportSchedScreen(
    petId: String,
    navHost: NavHostController,
    topPadding: Dp,
    viewModel: AddPassportSchedViewmodel = koinViewModel<AddPassportSchedViewmodel>()
) {

    val uiState by viewModel.uiState.collectAsState()
    val sched = uiState.sched

    val coroutineScope = rememberCoroutineScope()
    val showSubmitDialog = remember { mutableStateOf(false) }
    val showLeaveDialog = remember { mutableStateOf(false) }
    var isPassedDate by remember { mutableStateOf(false) }
    var enabledButton by remember { mutableStateOf(false) }

    var isHospitalLimitReached by remember { mutableStateOf(false) }
    var isNotesLimitReached by remember { mutableStateOf(false) }
    var isVaccineTypeLimitReached by remember { mutableStateOf(false) }
    var isVetLimitReached by remember { mutableStateOf(false) }
    val validation = sched!!.validate(isPassedDate)

    val sheetState = rememberModalBottomSheetState()
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var dob by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf("") }
    var isDateError by remember { mutableStateOf(false) }

    var selectedHour by remember { mutableStateOf("08") }
    var selectedMinute by remember { mutableStateOf("30") }
    var selectedAmPm by remember { mutableStateOf("AM") }

    LaunchedEffect(uiState.isSubmittingSuccess, uiState.isSubmitting) {
        enabledButton = !uiState.isSubmitting && !uiState.isSubmittingSuccess
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    showLeaveDialog.value = true
                },
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back_ios_new_24px),
                    contentDescription = "Back"
                )
            }

            TextButton(
                onClick = {
//                        showSubmitDialog.value = true
                    viewModel.updateIsSubmitting(true)
                },
                enabled = enabledButton,
            ) {
                Text(
                    text = "Submit",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
            }


        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add Vaccine Schedule",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    ThemeOutlineTextField(
                        value = sched.vaccineType,
                        label = "Vaccine Type",
                        placeholder = "e.g. 5-in-1 vaccine",
                        maxLength = 30,
                        onValueChange = { text ->
                            isVaccineTypeLimitReached = text.length >= 30
                            viewModel.updateVaccineType(text)
                        },
                        isError = isVaccineTypeLimitReached,
                        errorMessage = "Maximum character limit reached"
                    )

                    ThemeOutlineTextField(
                        value = sched.hospital,
                        label = "Hospital",
                        placeholder = "e.g. Cebu Vets Clinic",
                        maxLength = 50,
                        onValueChange = { text ->
                            isHospitalLimitReached = text.length >= 50
                            viewModel.updateHospital(text)
                        },
                        isError = isHospitalLimitReached,
                        errorMessage = "Maximum character limit reached"
                    )

                    Row {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)

                        ) {
                            ThemeClickableField(
                                value = formatDateForDisplay(sched.schedDateTime),
                                label = "Date",
                                placeholder = "e.g. May 15, 2020",
                                isError = isDateError,
                                errorMessage = dateError,
                                onClick = {
                                    println("Date picker clicked! 2") // Add this line
                                    Napier.i { "Date picker clicked! 2" }
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
                                value = "${selectedHour}:${selectedMinute} ${selectedAmPm}",
                                label = "Time",
                                placeholder = "e.g. 10:00 AM",
                                isError = false,
                                errorMessage = "",
                                onClick = { showTimePicker = true }

                            )
                        }

                    }

                    ThemeOutlineTextField(
                        value = sched.notes,
                        label = "Notes",
                        placeholder = "e.g. Annual vaccine checkup",
                        maxLength = 100,
                        onValueChange = { text ->
                            isNotesLimitReached = text.length >= 100
                            viewModel.updateNotes(text)
                        },
                        isError = isNotesLimitReached,
                        errorMessage = "Maximum character limit reached",
                        maxLines = 5,
                        singleLine = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                    )


                }
            }

            if (isPassedDate) {

                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ThemeOutlineTextField(
                            value = sched.vet ?: "",
                            label = "Veterinarian",
                            placeholder = "e.g. Dr. Maria Santos",
                            maxLength = 30,
                            onValueChange = { text ->
                                isVetLimitReached = text.length >= 30
                                viewModel.updateVet(text)
                            },
                            isError = isVetLimitReached,
                            errorMessage = "Maximum character limit reached"
                        )
                    }

                }
            }


        }

    }

    if (showSubmitDialog.value) {
        AlertDialog(
            onDismissRequest = { showSubmitDialog.value = false },
            title = { Text("Confirm Submission") },
            text = { Text("Are you sure all info are correct?") },
            confirmButton = {
                TextButton(onClick = {

//                    viewModel.updateIsSubmitting(true)
                    showSubmitDialog.value = false
//                    viewModel.submitPet()

                }) {
                    Text("Yes, Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog.value = false }) {
                    Text(text = "Cancel", color = Color.Gray)
                }
            }
        )

    }

    if (showLeaveDialog.value) {
        AlertDialog(
            onDismissRequest = { showLeaveDialog.value = false },
            title = { Text("Discard adding of appointment?") },
            text = { Text("If you leave now, any unsaved changes will be lost. Are you sure you want to discard and leave?") },
            dismissButton = {
                TextButton(onClick = { showLeaveDialog.value = false }) {
                    Text(text = "Continue Editing", color = Color.Gray)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    navHost.popBackStack()
                }) {
                    Text("Discard & Leave")
                }
            },
        )
    }

    if (uiState.isSubmitting) {
        Dialog(
            onDismissRequest = { viewModel.updateIsSubmitting(false) },
            content = {
                UploadingScreen(
                    title = "Almost Done!",
                    message = "We're finalizing your pet’s appointment—just a few tail wags away!"
                )
            }
        )
    }

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
                            isPassedDate = true
                        }

                        else -> {
                            val maxAgeDate =
                                currentDate.minus(
                                    50,
                                    DateTimeUnit.YEAR
                                )

                            if (selectedDate < maxAgeDate) {
                                dateError = "Please enter a reasonable date"
                                isDateError = true
                            } else {
                                dateError = ""
                                dob = formatDateToString(selectedDate)
                                isDateError = false
                                viewModel.updateSchedDateTime(
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
                    items = (0..59).map { it.toString().padStart(2, '0') },
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

}

@Preview
@Composable
fun AddPassportSchedScreenPreview() {

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {

        TailyTheme {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            ) {

                AddPassportSchedScreen(
                    petId = "",
                    navHost = rememberNavController(),
                    topPadding = 0.dp,
                    viewModel = koinViewModel<AddPassportSchedViewmodel>()
                )

            }
        }
    }
}
package org.jikisan.taily.ui.screens.addpet.addpetcontent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Calendar
import androidx.compose.ui.unit.dp
import com.vidspark.androidapp.ui.theme.TailyTheme
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.AddPetHeader
import org.jikisan.taily.ui.screens.addpet.AddPetViewModel
import org.jikisan.taily.util.DateUtils.convertToISO_MPP
import org.jikisan.taily.util.DateUtils.formatDateToString
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.calendar_month_24px

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetDOBContent(viewModel: AddPetViewModel, pet: Pet?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        val datePickerState = rememberDatePickerState()
        var showDatePicker by remember { mutableStateOf(false) }
        var dob by remember { mutableStateOf("") }
        var dateError by remember { mutableStateOf<String?>(null) }
        var isDateError by remember { mutableStateOf(false) }

        AddPetHeader("Enter ${pet?.name ?: "pet"}'s birthdate")

        Spacer(modifier = Modifier.height(8.dp))

        ThemeOutlineTextField(
            value = dob,
            onValueChange = { /* Read-only field */ },
            placeholder = "Date of Birth",
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
            modifier = Modifier.clickable { showDatePicker = true },
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
                                val maxAgeDate = currentDate.minus(50, DateTimeUnit.YEAR)

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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    datePickerState: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
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
        DatePicker(
            state = datePickerState,
            modifier = Modifier.padding(16.dp)
        )
    }
}


@Preview
@Composable
fun AddPetDOBContent(viewModel: AddPetViewModel) {

    TailyTheme {
        AddPetDOBContent(viewModel, viewModel.uiState.value.pet)
    }
}
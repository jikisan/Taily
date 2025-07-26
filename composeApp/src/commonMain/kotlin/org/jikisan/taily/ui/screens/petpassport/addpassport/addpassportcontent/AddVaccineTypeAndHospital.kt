package org.jikisan.taily.ui.screens.petpassport.addpassport.addpassportcontent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vidspark.androidapp.ui.theme.Blue
import com.vidspark.androidapp.ui.theme.OffBlue
import org.jikisan.taily.model.pet.Schedule
import org.jikisan.taily.ui.components.ThemeClickableField
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.AddPetHeader
import org.jikisan.taily.ui.screens.petpassport.addpassport.AddPassportSchedViewmodel
import org.jikisan.taily.ui.screens.petpassport.addpassport.addpassportcontent.PassportConstant.GIVEN_TYPES
import org.jikisan.taily.ui.screens.petpassport.addpassport.addpassportcontent.PassportConstant.VACCINE_TYPES

@Composable
fun AddVaccineTypeAndHospital(viewModel: AddPassportSchedViewmodel, sched: Schedule) {

    var showVaccineTypePicker by remember { mutableStateOf(false) }
    var isVaccineTypeLimitReached by remember { mutableStateOf(false) }
    var showOtherVaccineTextField by remember { mutableStateOf(false) }
    var isOtherVaccineTypeLimitReached by remember { mutableStateOf(false) }

    var isHospitalLimitReached by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            AddPetHeader("Enter the Vaccine and Hospital")

            // Vaccine Type
            ThemeClickableField(
                value = if (!showOtherVaccineTextField) sched.vaccineType else "Other",
                label = "\uD83D\uDC8A Vaccine",
                placeholder = "e.g. 5-in-1 vaccine",
                borderColor = Blue,
                isError = isVaccineTypeLimitReached,
                errorMessage = "Maximum character limit reached",
                onClick = {
                    showVaccineTypePicker = true
                }
            )

            if (showOtherVaccineTextField) {
                ThemeOutlineTextField(
                    value = sched.vaccineType ?: "",
                    label = "\uD83D\uDC8A Other Vaccine",
                    placeholder = "Enter how the vaccine was given",
                    borderColor = Blue,
                    maxLength = 30,
                    onValueChange = { text ->
                        isOtherVaccineTypeLimitReached = text.length >= 30
                        viewModel.updateVaccineType(text)
                    },
                    isError = isOtherVaccineTypeLimitReached,
                    errorMessage = "Maximum character limit reached",
                )
            }

            DropdownMenu(
                expanded = showVaccineTypePicker,
                onDismissRequest = { showVaccineTypePicker = false },
                modifier = Modifier.width(200.dp),
                shape = RoundedCornerShape(10),
                containerColor = OffBlue,
                border = BorderStroke(1.dp, Blue),
            ) {
                VACCINE_TYPES.forEach { givenType ->
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
                                showOtherVaccineTextField = true
                                viewModel.updateVaccineType("")
                            } else {
                                showOtherVaccineTextField = false
                                viewModel.updateVaccineType(givenType)
                            }
                            showVaccineTypePicker = false
                        },
                        modifier = Modifier.fillMaxWidth(),

                        )
                }
            }

            // Hospital
            ThemeOutlineTextField(
                value = sched.hospital ?: "",
                label = " \uD83C\uDFE5 Hospital",
                placeholder = "e.g. Cebu Vets Clinic",
                borderColor = Blue,
                maxLength = 50,
                onValueChange = { text ->
                    isHospitalLimitReached = text.length >= 50
                    viewModel.updateHospital(text)
                },
                isError = isHospitalLimitReached,
                errorMessage = "Maximum character limit reached"
            )



        }
    }
}
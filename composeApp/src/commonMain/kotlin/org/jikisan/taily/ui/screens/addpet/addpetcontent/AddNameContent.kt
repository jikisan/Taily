package org.jikisan.taily.ui.screens.addpet.addpetcontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.AddPetViewModel
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNameContent(viewModel: AddPetViewModel, pet: Pet?) {

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "What's your pet's name?",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))

        ThemeOutlineTextField(
            value = pet?.name ?: "",
            placeholder = "Type pet's name",
            onValueChange = { petName: String -> viewModel.updateName(petName) },
        )


//
//        Spacer(modifier = Modifier.height(24.dp))

        // Name field


//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Gender selection
//        Text(
//            text = "Gender",
//            style = MaterialTheme.typography.bodyMedium,
//            fontWeight = FontWeight.Medium
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            GENDER_OPTIONS.forEach { gender: String ->
//                FilterChip(
//                    onClick = { viewModel.updateGender(gender) },
//                    label = { Text(gender) },
//                    selected = pet?.gender == gender,
//                    modifier = Modifier.weight(1f)
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        var dob by remember { mutableStateOf("") }
//        // Weight field
//
//        // Date of birth
//        ThemeOutlineTextField(
//            value = dob,
//            onValueChange = {
//                dob = it
//                viewModel.updateDateOfBirth(dob)
//            },
//            placeholder = "Date of Birth",
//            readOnly = true,
//            leadingIcon = {
//                Icon(
//                    painter = painterResource(Res.drawable.stethoscope_24px),
//                    contentDescription = null
//                )
//            },
//            trailingIcon = {
//                Icon(
//                    painter = painterResource(Res.drawable.stethoscope_24px),
//                    contentDescription = null
//                )
//            }
//        )
//
//        if (showDatePicker) {
//            DatePickerDialog(
//                onDateSelected = { date ->
//                    viewModel.updateDateOfBirth(date)
//                    showDatePicker = false
//                },
//                onDismiss = { showDatePicker = false }
//            )
//        }
    }
}

@Preview
@Composable
fun AddNameContentPreview() {
    TailyTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {

            AddNameContent(
                viewModel = koinViewModel<AddPetViewModel>(),
                pet = null
            )
        }
    }
}
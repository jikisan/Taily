package org.jikisan.taily.ui.screens.addpet.addpetcontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.AddPetHeader
import org.jikisan.taily.ui.screens.addpet.AddPetViewModel
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNameContent(viewModel: AddPetViewModel, pet: Pet?) {

    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        AddPetHeader("What's your pet's name?")

        Spacer(modifier = Modifier.height(8.dp))

        ThemeOutlineTextField(
            value = pet?.name ?: "",
            placeholder = "Type pet's name",
            onValueChange = { petName: String ->

                    if (petName.length >= 20) {
                        isError = true
                    } else {
                        isError = false
                    }

                viewModel.updateName(petName)


            },
            isError = isError
        )

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
package org.jikisan.taily.ui.screens.addpet.addpetcontent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.AddPetHeader
import org.jikisan.taily.ui.screens.addpet.AddPetViewModel
import org.jikisan.taily.ui.screens.addpet.PetConstants.PET_TYPES
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.arrow_drop_down_24px

@Composable
fun AddPetSpeciesContent(viewModel: AddPetViewModel, pet: Pet?) {

    var showSpeciesPicker by remember { mutableStateOf(false) }
    var customSpecies by remember { mutableStateOf("") }
    var showCustomField by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AddPetHeader("Tell us about ${pet?.name ?: "your pet"}'s species")

        Spacer(modifier = Modifier.height(8.dp))

        // Main species dropdown field
        Box {
            ThemeOutlineTextField(
                value = if (showCustomField) "Other" else pet?.petType ?: "",
                onValueChange = { /* Read-only field */ },
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

                                    if (petType == "Other") {
                                        showCustomField = true
                                        // Don't update viewModel yet, wait for custom input
                                    } else {
                                        showCustomField = false
                                        customSpecies = ""
                                        viewModel.updatePetType(petType)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),

                                )
                        }
                    }
                },
                modifier = Modifier.clickable { showSpeciesPicker = !showSpeciesPicker },
                showCharacterCounter = false,
            )

            // Dropdown menu

        }

        // Custom species text field (appears when "Other" is selected)
        if (showCustomField) {
            Spacer(modifier = Modifier.height(16.dp))

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

@Preview
@Composable
fun AddPetSpeciesContentPreview() {
    TailyTheme {
        AddPetSpeciesContent(
            viewModel = koinViewModel<AddPetViewModel>(),
            pet = null
        )
    }
}
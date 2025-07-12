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
import org.jetbrains.compose.resources.painterResource
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.AddPetHeader
import org.jikisan.taily.ui.screens.addpet.AddPetViewModel
import org.jikisan.taily.ui.screens.addpet.PetConstants.PET_BREEDS
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.arrow_drop_down_24px

@Composable
fun AddPetBreedContent(viewModel: AddPetViewModel, pet: Pet?) {

    var showBreedPicker by remember { mutableStateOf(false) }
    var customBreed by remember { mutableStateOf("") }
    var showCustomField by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AddPetHeader("How about ${pet?.name ?: "your pet"}'s breed")

        Spacer(modifier = Modifier.height(8.dp))

        // Main breed dropdown field
        Box {
            ThemeOutlineTextField(
                value = if (showCustomField) "Other" else pet?.breed ?: "",
                onValueChange = { /* Read-only field */ },
                placeholder = "Enter your pet's breed",
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showBreedPicker = !showBreedPicker }) {
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
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
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

                                        if (it.equals("Other", ignoreCase = true) || petType.equals("Other", ignoreCase = true)) {
                                            showCustomField = true
                                            // Don't update viewModel yet, wait for custom input
                                        } else {
                                            showCustomField = false
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
                modifier = Modifier.clickable { showBreedPicker = !showBreedPicker },
                showCharacterCounter = false,
            )

            // Dropdown menu

        }

        // Custom breed text field (appears when "Other" is selected)
        if (showCustomField) {
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
            )
        }
    }
}
package org.jikisan.taily.ui.screens.addpet.addpetcontent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.Identifiers
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.AddPetHeader
import org.jikisan.taily.ui.screens.addpet.AddPetViewModel
import org.jikisan.taily.ui.screens.addpet.PetConstants.CLIP_LOCATIONS
import org.jikisan.taily.ui.screens.addpet.PetConstants.MICROCHIP_LOCATIONS
import org.jikisan.taily.ui.screens.addpet.PetConstants.SIZE_OPTIONS
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun AddPetIdentifiersContent(
    viewModel: AddPetViewModel,
    pet: Pet?
) {
    val scrollState = rememberScrollState()

    var hasMicrochip by remember { mutableStateOf(pet?.identifiers?.microchipNumber != null) }
    var microchipNumber by remember { mutableStateOf(pet?.identifiers?.microchipNumber ?: "") }
    var microchipLocation by remember { mutableStateOf(pet?.identifiers?.microchipLocation ?: "") }
    var clipLocation by remember { mutableStateOf(pet?.identifiers?.clipLocation ?: "") }
    var size by remember { mutableStateOf(pet?.identifiers?.size ?: "") }
    var colorMarkings by remember { mutableStateOf(pet?.identifiers?.colorMarkings ?: "") }
    var isNeuteredOrSpayed by remember {
        mutableStateOf(
            pet?.identifiers?.isNeuteredOrSpayed ?: false
        )
    }
    val allergies = remember {
        mutableStateListOf<String>().apply {
            addAll(
                pet?.identifiers?.allergies ?: emptyList()
            )
        }
    }
    var newAllergy by remember { mutableStateOf("") }

    // Save updated identifiers to viewModel
    LaunchedEffect(
        microchipNumber,
        microchipLocation,
        clipLocation,
        size,
        colorMarkings,
        isNeuteredOrSpayed,
        allergies.toList(),
        hasMicrochip
    ) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AddPetHeader("Pet Identifiers")

        // Microchip toggle
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
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
                    value = microchipNumber,
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
                    selectedOption = microchipLocation,
                    onOptionSelected = { microchipLocation = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }

        // Clip Location
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                DropdownMenuField(
                    label = "Clip Location",
                    options = CLIP_LOCATIONS,
                    selectedOption = clipLocation,
                    onOptionSelected = { clipLocation = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Size
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()

        ) {
           Column(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(16.dp)
           ) {
               DropdownMenuField(
                   label = "Size",
                   options = SIZE_OPTIONS,
                   selectedOption = size,
                   onOptionSelected = { size = it },
                   modifier = Modifier.fillMaxWidth()
               )
           }
        }

        // Color Markings
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Color Markings", style = MaterialTheme.typography.bodyMedium)
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

        // Neutered/Spayed toggle
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
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

        // Allergies input
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Allergies", style = MaterialTheme.typography.bodyMedium)
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
                        maxLength = 30,
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (newAllergy.isNotBlank() && !allergies.contains(newAllergy.trim())) {
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


        }
    }


}


@Composable
fun AllergyList(
    allergies: List<String>,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        allergies.forEach { allergy ->
            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = allergy,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove allergy",
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onRemove(allergy) },
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedOption.ifEmpty { "Select..." })
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun AddPetIdentifiersContentPreview() {

    TailyTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(16.dp)){
            AddPetIdentifiersContent(
                viewModel = koinViewModel<AddPetViewModel>(),
                pet = null
            )

        }

    }
}

fun sizeWeightGuide(size: String): String {
    return when (size) {
        "Small" -> "Approx. 0-10kg (0-22lbs)"
        "Medium" -> "Approx. 10-25kg (22-55lbs)"
        "Large" -> "Approx. 25-40kg (55-88lbs)"
        "Extra Large" -> "Over 40kg (88lbs+)"
        else -> ""
    }
}
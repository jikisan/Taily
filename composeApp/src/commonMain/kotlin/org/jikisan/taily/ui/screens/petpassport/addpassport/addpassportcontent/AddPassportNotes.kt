package org.jikisan.taily.ui.screens.petpassport.addpassport.addpassportcontent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vidspark.androidapp.ui.theme.Blue
import org.jikisan.taily.model.pet.Schedule
import org.jikisan.taily.ui.components.ThemeOutlineTextField
import org.jikisan.taily.ui.screens.addpet.AddPetHeader
import org.jikisan.taily.ui.screens.petpassport.addpassport.AddPassportSchedViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPassportNotes(viewModel: AddPassportSchedViewmodel, sched: Schedule) {

    var isNotesLimitReached by remember { mutableStateOf(false) }

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

            AddPetHeader("Add some notes (Optional)")

            // Notes
            ThemeOutlineTextField(
                value = sched.notes,
                label = "\uD83D\uDCDD Notes",
                placeholder = "e.g. Annual vaccine checkup",
                borderColor = Blue,
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

}
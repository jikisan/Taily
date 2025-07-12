package org.jikisan.taily.ui.screens.addpet.addpetcontent

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.screens.addpet.AddPetHeader
import org.jikisan.taily.ui.screens.addpet.AddPetViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddPetSpeciesContent(viewModel: AddPetViewModel, pet: Pet?) {

    AddPetHeader("Tell us about ${pet?.name}''s species")

    Spacer(modifier = Modifier.height(8.dp))


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
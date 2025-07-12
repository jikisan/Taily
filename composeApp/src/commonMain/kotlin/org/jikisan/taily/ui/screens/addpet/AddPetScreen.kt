package org.jikisan.taily.ui.screens.addpet

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vidspark.androidapp.ui.theme.TailyTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.data.local.mockdata.MockData
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.domain.pet.PetRepository
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddNameContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddPetDOBContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddPetProfilePhotoContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddPetGenderContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddPetSpeciesContent
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.arrow_back_ios_new_24px
import taily.composeapp.generated.resources.happy_pet
import taily.composeapp.generated.resources.loading_paw

val PET_TYPES =
    listOf("Dog", "Cat", "Bird", "Fish", "Rabbit", "Hamster", "Guinea Pig", "Turtle", "Other")
val DOG_BREEDS = listOf(
    "Labrador",
    "Golden Retriever",
    "German Shepherd",
    "Bulldog",
    "Poodle",
    "Shih Tzu",
    "Beagle",
    "Rottweiler",
    "Siberian Husky",
    "Other"
)
val CAT_BREEDS = listOf(
    "Persian",
    "Maine Coon",
    "Siamese",
    "British Shorthair",
    "Ragdoll",
    "Bengal",
    "Abyssinian",
    "Russian Blue",
    "Scottish Fold",
    "Other"
)
val BIRD_BREEDS = listOf("Budgie", "Cockatiel", "Canary", "Lovebird", "Parrot", "Finch", "Other")
val WEIGHT_UNITS = listOf("kg", "lbs", "g", "oz")
val SIZE_OPTIONS = listOf("Small", "Medium", "Large", "Extra Large")
val MICROCHIP_LOCATIONS =
    listOf("Neck", "Between shoulder blades", "Left shoulder", "Right shoulder", "Other")
val CLIP_LOCATIONS = listOf("Left ear", "Right ear", "Both ears", "Tail", "Other")
val GENDER_OPTIONS = listOf("Male", "Female")

val CURRENT_PAGE_HEADER = listOf(
    "Pet's Name",
    "Pet's Profile Photo",
    "Pet's Gender",
    "Pet's Date of Birth",
    "Pet's species",
    "Pet's Breed",
    "Pet's Weight",
    "Identifiers"
)

@Composable
fun AddPetScreen(
    navHost: NavHostController,
    topPadding: Dp,
    viewModel: AddPetViewModel = koinViewModel<AddPetViewModel>()
) {
    val totalPages = CURRENT_PAGE_HEADER.size
    val pagerState = rememberPagerState(pageCount = { totalPages })
    val coroutineScope = rememberCoroutineScope()
    val currentPageHeader = remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(top = topPadding),
    ) {
        Text(
            text = "Add Pet Profile",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth().offset(y = 16.dp)
                .padding(top = 16.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        IconButton(
            onClick = {
                if (pagerState.currentPage <= 0) {
                    navHost.popBackStack()
                } else {

                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }

                }
            },
        ) {
            Icon(
                painter = painterResource(Res.drawable.arrow_back_ios_new_24px),
                contentDescription = "Back"
            )
        }

        Text(
            text = CURRENT_PAGE_HEADER[pagerState.currentPage],
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth().offset(y = (-16).dp),
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        LinearProgressIndicator(
            progress = { (pagerState.currentPage.toFloat() + 1) / totalPages.toFloat() },
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(5.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surface
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page: Int ->

            when (page) {
                0 -> AddNameContent(viewModel, uiState.pet)
                1 -> AddPetProfilePhotoContent(viewModel, uiState.pet)
                2 -> AddPetGenderContent(viewModel, pet = uiState.pet)
                3 -> AddPetDOBContent(viewModel, pet = uiState.pet)
                4 -> AddPetSpeciesContent(viewModel, pet = uiState.pet)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                modifier = Modifier.fillMaxWidth(),
                elevation = ButtonDefaults.buttonElevation(15.dp),
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage < totalPages - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            // Submit
                        }
                    }
                }) {
                Text(if (pagerState.currentPage + 1 == totalPages) "Submit" else "Next")
            }
        }
    }
}

@Preview
@Composable
fun AddPetScreenPreview() {
    // --- Provide a mock PetViewModel and Pet for preview ---

    TailyTheme {
        val dummyRepo = object : PetRepository {
            override suspend fun getPets() = flowOf(emptyList<Pet>())
            override suspend fun getPetsByUserId() =
                flowOf(emptyList<Pet>())
        }
        val pet = MockData.mockPets.first()
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {


            AddPetScreen(
                navHost = rememberNavController(),
                topPadding = 0.dp,
                viewModel = koinViewModel<AddPetViewModel>()
            )

        }
    }

}
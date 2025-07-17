package org.jikisan.taily.ui.screens.addpet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vidspark.androidapp.ui.theme.TailyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.data.local.mockdata.MockData
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.domain.pet.PetRepository
import org.jikisan.taily.domain.validator.validate
import org.jikisan.taily.ui.common.UploadingScreen
import org.jikisan.taily.ui.components.AnimatedGradientSnackbarHost
import org.jikisan.taily.ui.components.GradientSnackbar
import org.jikisan.taily.ui.components.SnackbarDuration
import org.jikisan.taily.ui.components.SnackbarType
import org.jikisan.taily.ui.screens.addpet.PetConstants.PET_BREEDS
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddNameContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddPetBreedContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddPetDOBContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddPetGenderContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddPetIdentifiersContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddPetProfilePhotoContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddPetSpeciesContent
import org.jikisan.taily.ui.screens.addpet.addpetcontent.AddPetWeightContent
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.arrow_back_ios_new_24px

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
    val showSubmitDialog = remember { mutableStateOf(false) }
    val showLeaveDialog = remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val pet = uiState.pet
    val validation = pet!!.validate(pagerState.currentPage)



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
                    showLeaveDialog.value = true


                } else {
                    coroutineScope.launch {

                        val hasBreeds = PET_BREEDS.containsKey(uiState.pet?.petType)

                        if (pagerState.currentPage == 6 && !hasBreeds) {
                            pagerState.scrollToPage(pagerState.currentPage - 2)
                        } else {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
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
            modifier = Modifier.weight(1f),
            userScrollEnabled = false
        ) { page: Int ->

            when (page) {
                0 -> AddNameContent(viewModel, pet)
                1 -> AddPetProfilePhotoContent(viewModel, pet)
                2 -> AddPetGenderContent(viewModel, pet = pet)
                3 -> AddPetDOBContent(viewModel, pet = pet)
                4 -> AddPetSpeciesContent(viewModel, pet = pet)
                5 -> AddPetBreedContent(viewModel, pet = pet)
                6 -> AddPetWeightContent(viewModel, pet = pet)
                7 -> AddPetIdentifiersContent(viewModel, pet = pet)
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
                enabled = !uiState.isSubmitting, // Disable when uploading is true
                onClick = {
                    coroutineScope.launch {

                        if (validation.isValid) {
                            if (pagerState.currentPage < totalPages - 1) {

                                val hasBreeds = PET_BREEDS.containsKey(uiState.pet?.petType)
                                if (!hasBreeds && pagerState.currentPage == 4) {
                                    pagerState.scrollToPage(pagerState.currentPage + 2)
                                } else {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }

                            } else {
                                showSubmitDialog.value = true
                            }
                        } else {
                            viewModel.updateErrorMessage(validation.error!!)
                        }


                    }
                }
            ) {

                if (uiState.isSubmitting || uiState.isSubmittingSuccess) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(if (pagerState.currentPage + 1 == totalPages) "Submit" else "Next")
                }
            }
        }
    }

    if (showSubmitDialog.value) {
        AlertDialog(
            onDismissRequest = { showSubmitDialog.value = false },
            title = { Text("Confirm Submission") },
            text = { Text("Are you sure all pet info are correct?") },
            confirmButton = {
                TextButton(onClick = {

                    viewModel.updateIsSubmitting(true)
                    showSubmitDialog.value = false
                    viewModel.submitPet()

                }) {
                    Text("Yes, Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitDialog.value = false }) {
                    Text(text = "Cancel", color = Color.Gray)
                }
            }
        )

    }

    if (showLeaveDialog.value) {
        AlertDialog(
            onDismissRequest = { showLeaveDialog.value = false },
            title = { Text("Discard New Pet?") },
            text = { Text("You are currently adding a new pet. If you leave now, any unsaved changes will be lost. Are you sure you want to discard and leave?") },
            dismissButton = {
                TextButton(onClick = {
                    navHost.popBackStack()
                }) {
                    Text("Discard & Leave", color = Color.Gray)
                }
            },
            confirmButton = {
                TextButton(onClick = { showLeaveDialog.value = false }) {
                    Text(text = "Continue Editing")
                }
            },


            )
    }

    if (uiState.isSubmitting) {
        Dialog(
            onDismissRequest = { viewModel.updateIsSubmitting(false) },
            content = { UploadingScreen() }
        )
    }

    if (uiState.errorMessage != null) {
        AnimatedGradientSnackbarHost(
            message = uiState.errorMessage!!,
            type = SnackbarType.ERROR,
            onDismiss = { viewModel.updateErrorMessage(null) },
            duration = SnackbarDuration.LONG
        )
    }

    if (uiState.isSubmittingSuccess) {
        AnimatedGradientSnackbarHost(
            message = "Your pet has been added successfully!",
            type = SnackbarType.SUCCESS,
            onDismiss = {
                navHost.popBackStack()
            },
        )
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
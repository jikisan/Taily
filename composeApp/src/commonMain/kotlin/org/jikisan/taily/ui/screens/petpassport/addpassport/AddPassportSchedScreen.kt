package org.jikisan.taily.ui.screens.petpassport.addpassport

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import com.vidspark.androidapp.ui.theme.Blue
import com.vidspark.androidapp.ui.theme.OffBlue
import com.vidspark.androidapp.ui.theme.TailyTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.ui.common.UploadingScreen
import org.jikisan.taily.ui.screens.petpassport.addpassport.addpassportcontent.AddPassportDateTime
import org.jikisan.taily.ui.screens.petpassport.addpassport.addpassportcontent.AddPassportNotes
import org.jikisan.taily.ui.screens.petpassport.addpassport.addpassportcontent.AddVaccineTypeAndHospital
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.arrow_back_ios_new_24px

val CURRENT_PAGE_HEADER = listOf(
    "\uD83E\uDDEA Vaccine Type & Hospital",
    "\uD83D\uDCDD Notes",
    "\uD83D\uDDD3 Date & Time"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPassportSchedScreen(
    petId: String,
    navHost: NavHostController,
    topPadding: Dp,
    viewModel: AddPassportSchedViewmodel = koinViewModel<AddPassportSchedViewmodel>()
) {

    val uiState by viewModel.uiState.collectAsState()
    val sched = uiState.sched
    val scrollState = rememberScrollState()

    val totalPages = CURRENT_PAGE_HEADER.size
    val pagerState = rememberPagerState(pageCount = { totalPages })

    val coroutineScope = rememberCoroutineScope()
    val showSubmitDialog = remember { mutableStateOf(false) }
    val showLeaveDialog = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding),
    ) {

        Text(
            text = "Add Vaccine Schedule",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth().offset(y = 16.dp)
                .padding(top = 16.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    if (pagerState.currentPage <= 0) {
                        showLeaveDialog.value = true
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

        }

        Text(
            text = CURRENT_PAGE_HEADER[pagerState.currentPage],
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth().offset(y = (-16).dp),
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
//                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = { (pagerState.currentPage.toFloat() + 1) / totalPages.toFloat() },
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(5.dp)
                    .weight(4f)
                    .clip(RoundedCornerShape(5.dp)),
                color = Blue,
                trackColor = OffBlue
            )

            Text(
                text = " ${pagerState.currentPage + 1} / $totalPages",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                modifier = Modifier.weight(1f)
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            userScrollEnabled = false
        ) { page: Int ->

            when (page) {
                0 -> sched?.let { AddVaccineTypeAndHospital(viewModel, sched = it) }
                1 -> sched?.let { AddPassportNotes(viewModel, it) }
                2 -> sched?.let { AddPassportDateTime(viewModel, sched = it) }
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
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = !uiState.isSubmitting, // Disable when uploading is true
                onClick = {
                    coroutineScope.launch {

//                        if (validation.isValid) {
                            if (pagerState.currentPage < totalPages - 1) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)

                            } else {
                                showSubmitDialog.value = true
                            }
//                        } else {
//                            viewModel.updateErrorMessage(validation.error!!)
//                        }


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
            text = { Text("Are you sure all info are correct?") },
            confirmButton = {
                TextButton(onClick = {

                    viewModel.updateIsSubmitting(true)
                    showSubmitDialog.value = false
//                    viewModel.submitPet()

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
            title = { Text("Discard adding of appointment?") },
            text = { Text("If you leave now, any unsaved changes will be lost. Are you sure you want to discard and leave?") },
            dismissButton = {
                TextButton(onClick = { showLeaveDialog.value = false }) {
                    Text(text = "Continue Editing", color = Color.Gray)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    navHost.popBackStack()
                }) {
                    Text("Discard & Leave")
                }
            },
        )
    }

    if (uiState.isSubmitting) {
        Dialog(
            onDismissRequest = { viewModel.updateIsSubmitting(false) },
            content = {
                UploadingScreen(
                    title = "Almost Done!",
                    message = "We're finalizing your pet’s appointment—just a few tail wags away!"
                )
            }
        )
    }


}

@Preview
@Composable
fun AddPassportSchedScreenPreview() {

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {

        TailyTheme {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            ) {

                AddPassportSchedScreen(
                    petId = "",
                    navHost = rememberNavController(),
                    topPadding = 0.dp,
                    viewModel = koinViewModel<AddPassportSchedViewmodel>()
                )

            }
        }
    }
}
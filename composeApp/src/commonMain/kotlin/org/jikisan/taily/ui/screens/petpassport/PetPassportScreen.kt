package org.jikisan.taily.ui.screens.petpassport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vidspark.androidapp.ui.theme.Blue
import com.vidspark.androidapp.ui.theme.OffBlue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.ui.common.EmptyScreen
import org.jikisan.taily.ui.common.ErrorScreen
import org.jikisan.taily.ui.common.LoadingScreen
import org.jikisan.taily.ui.common.ScheduleItemCard
import org.jikisan.taily.ui.components.Header
import org.jikisan.taily.ui.navigation.NavigationItem
import org.jikisan.taily.ui.screens.pet.PetViewModel
import org.jikisan.taily.ui.uistates.PassportUIState
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.add_2_24px
import taily.composeapp.generated.resources.arrow_back_ios_new_24px
import taily.composeapp.generated.resources.happy_pet
import taily.composeapp.generated.resources.qr_code_24px
import taily.composeapp.generated.resources.sad_cat

@Composable
fun PetPassportScreen(
    petId: String,
    navHost: NavHostController,
    topPadding: Dp,
    viewModel: PetPassportViewModel = koinViewModel<PetPassportViewModel>()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Upcoming", "History")


    LaunchedEffect(Unit) {
        viewModel.loadPetPassport(petId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            IconButton(
                onClick = { navHost.popBackStack() }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.arrow_back_ios_new_24px),
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

            TextButton(
                onClick = {  }
            ) {
                Icon(
                    painter = painterResource(Res.drawable.add_2_24px),
                    contentDescription = "Add pet",
                    tint = Blue,
                    modifier = Modifier.size(16.dp),
                )

                Text(
                    text = "Add",
                    color = Blue,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

        }

        Spacer(modifier = Modifier.height(8.dp))

        // Header
        uiState.pet?.let {
            Header(
                headerText = "${it.name}'s Passport",
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        when {

            uiState.isLoading && uiState.pet?.passport?.schedules == null -> {
                LoadingScreen(0.dp)
            }

            uiState.errorMessage != null && uiState.pet?.passport?.schedules == null -> {
                ErrorScreen(
                    errorMessage = uiState.errorMessage,
                    onClick = { viewModel.loadPetPassport(petId) }
                )
            }

            uiState.pet?.passport?.schedules.isNullOrEmpty() -> {
                EmptyScreen(
                    message = "No Passport Reminders",
                    resource = Res.drawable.happy_pet,
                    modifier = Modifier.padding(0.dp).fillMaxSize()
                )
            }

            else -> {

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = Blue,
                    indicator = { tabPositions ->
                        HorizontalDivider(color = Blue, thickness = 2.dp, modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]))
                    },
                    divider = {
                        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(text = title) }
                        )
                    }
                }

                when (selectedTabIndex) {
                    0 -> { // Upcoming
                        UpcomingList(uiState)
                    }
                    1 -> { // History
                        // TODO: Implement History tab content
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("History Content Here")
                        }
                    }
                }






            }
        }
    }


}

@Composable
private fun UpcomingList(uiState: PassportUIState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        uiState.schedule?.let {
            items(it.size) { index ->
                ScheduleItemCard(schedule = it[index])
            }
        }
    }
}

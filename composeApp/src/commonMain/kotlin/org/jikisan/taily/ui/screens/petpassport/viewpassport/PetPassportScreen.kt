package org.jikisan.taily.ui.screens.petpassport.viewpassport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.vidspark.androidapp.ui.theme.Blue
import org.jetbrains.compose.resources.painterResource
import org.jikisan.taily.domain.model.enum.FilterType
import org.jikisan.taily.model.pet.Schedule
import org.jikisan.taily.ui.common.EmptyScreen
import org.jikisan.taily.ui.common.ErrorScreen
import org.jikisan.taily.ui.common.LoadingScreen
import org.jikisan.taily.ui.common.ScheduleItemCard
import org.jikisan.taily.ui.navigation.NavigationItem
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.add_2_24px
import taily.composeapp.generated.resources.arrow_back_ios_new_24px
import taily.composeapp.generated.resources.happy_pet

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
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
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
                onClick = { navHost.navigate(NavigationItem.AddPetPassport.route.replace("{petId}", petId)) },
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
            Text(
                text = "${it.name}'s Health Passport",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = Blue,
            indicator = { tabPositions ->
                HorizontalDivider(
                    color = Blue,
                    thickness = 2.dp,
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
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

                when (selectedTabIndex) {
                    0 -> { // Upcoming
                        val todayAndUpcomingReminders = viewModel.getTodayAndUpcomingReminders()

                        if (todayAndUpcomingReminders.isNullOrEmpty()) {
                            EmptyScreen("No Upcoming Appointments", Res.drawable.happy_pet)
                        } else {
                            ScheduleList(schedulesList = todayAndUpcomingReminders, filterType = FilterType.UPCOMING)
                        }
                    }

                    1 -> {
                        val historyReminders = viewModel.getHistoryReminders()

                        if (historyReminders.isNullOrEmpty()) {
                            EmptyScreen("No History Reminders", Res.drawable.happy_pet)
                        } else {
                            ScheduleList(schedulesList = historyReminders, filterType = FilterType.HISTORY)
                        }
                    }

                }

            }
        }


    }


}

@Composable
private fun ScheduleList(schedulesList: List<Schedule>, filterType: FilterType) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(schedulesList) { schedules ->
            ScheduleItemCard(schedule = schedules, scheduleType = filterType)
        }
    }
}

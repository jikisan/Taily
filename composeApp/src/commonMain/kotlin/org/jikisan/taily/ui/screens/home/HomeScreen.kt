package org.jikisan.taily.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.vidspark.androidapp.ui.theme.OffBlue
import io.github.aakira.napier.Napier
import io.github.chouaibmo.rowkalendar.RowKalendar
import io.github.chouaibmo.rowkalendar.components.DateCell
import io.github.chouaibmo.rowkalendar.components.DateCellDefaults
import io.github.chouaibmo.rowkalendar.extensions.now
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.jikisan.taily.domain.model.ReminderList
import org.jikisan.taily.ui.components.Header
import org.jikisan.taily.util.DateUtils.formatDateForDisplayWithDayOfWeek
import org.jikisan.taily.util.DateUtils.formatToTime
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.notifications_24px
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    modifier: Modifier,
    topPadding: Dp,
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>()
) {

    val uiState by viewModel.uiState.collectAsState()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = { viewModel.refresh() },
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = topPadding, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {


            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray, shape = CircleShape)
                )
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.notifications_24px),
                        contentDescription = "Notification Icon",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            selectedDate?.let { date ->
                Text(
                    text = formatDateForDisplayWithDayOfWeek(date),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.LightGray,
                    textAlign = TextAlign.Left
                )
            }

            Header("Reminders", modifier = Modifier.padding(top = 0.dp))

            RowKalendar(
                modifier = Modifier.height(100.dp),
                content = { date, isSelected, onClick ->
                    DateCell(
                        date = date,
                        isSelected = isSelected,
                        onDateSelected = {
                            selectedDate = date
                            onClick(date)
                        },
                        modifier = Modifier.padding(2.dp),
                        shape = RoundedCornerShape(25.dp),
                        elevation = DateCellDefaults.DateCellElevation(
                            selectedElevation = 4.dp,
                            pastElevation = 0.dp,
                            futureElevation = 0.dp
                        ),
                        colors = DateCellDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.surface,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            pastContainerColor = Color.Transparent,
                            pastTextColor = Color.LightGray,
                            futureContainerColor = Color.Transparent,
                            futureTextColor = Color.LightGray
                        ),
                    )

                }
            )

            when {
                uiState.isLoading && uiState.pets.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.errorMessage != null && uiState.pets.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error: ${uiState.errorMessage}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text("Retry")
                        }
                    }
                }

                uiState.reminders.isNotEmpty() -> {

                    val currentTime = kotlinx.datetime.Clock.System.now()
                    val upcomingReminders = uiState.reminders.filter { reminderList ->
                        try {
                            val reminderInstant = Instant.parse(reminderList.dateTime)
                            reminderInstant > currentTime
                        } catch (e: Exception) {
                            Napier.e("Error parsing reminder dateTime: ${reminderList.dateTime}", e)
                            false
                        }
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(uiState.reminders) { reminderList ->

                            println("[DATETIME] DateTime: ${reminderList.dateTime} \n Selected Date: $selectedDate")


                            ReminderItemCard(reminderList = reminderList)
                        }
                    }
                }
            }
        }

    }

}

@Composable
fun ReminderItemCard(reminderList: ReminderList) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {


        Row(
            modifier = Modifier.height(IntrinsicSize.Min), // 👈 Important
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier
                    .widthIn(min = 50.dp),
                horizontalAlignment = Alignment.End,
            ) {

                formatToTime(reminderList.dateTime)?.let { time ->
                    val timeSplit = time.split(" ")
                    val time = timeSplit[0]
                    val amPm = timeSplit[1]

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = time,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            ),
                        )
                        Text(
                            text = amPm,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.LightGray
                            )
                        )
                    }


                }

            }

            Box(
                Modifier.padding(horizontal = 16.dp).fillMaxHeight(),
            ) {
                Canvas(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                ) {
                    drawRoundRect(
                        color = OffBlue,
                        cornerRadius = CornerRadius(x = size.width / 2, y = size.width / 2)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                reminderList.reminders.forEach { reminder ->
                    ReminderCard(reminder = reminder)
                }
            }
        }

    }
}




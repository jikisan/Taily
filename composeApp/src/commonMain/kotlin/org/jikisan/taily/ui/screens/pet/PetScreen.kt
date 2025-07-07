package org.jikisan.taily.ui.screens.pet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.data.local.mockdata.MockData.mockPets
import org.jikisan.taily.ui.common.EmptyScreen
import org.jikisan.taily.ui.common.ErrorScreen
import org.jikisan.taily.ui.components.Header
import org.jikisan.taily.ui.common.LoadingScreen
import org.jikisan.taily.ui.navigation.NavigationItem
import org.jikisan.taily.ui.screens.home.PetCard
import org.jikisan.taily.ui.uistates.PetUIState
import org.koin.compose.viewmodel.koinViewModel
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.add_2_24px
import taily.composeapp.generated.resources.sad_cat

@Composable
fun PetScreen(
    navHostController: NavHostController,
    modifier: Modifier,
    topPadding: Dp,
    viewModel: PetViewModel = koinViewModel<PetViewModel>()
) {

    val uiState by viewModel.uiState.collectAsState()
    PetScreenContent(
        uiState = uiState,
        onLoadPets = { viewModel.loadPets() },
        onRefresh = { viewModel.refreshPets() },
        navHost = navHostController,
        modifier = modifier.padding(top = topPadding)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PetScreenContent(
    uiState: PetUIState,
    onLoadPets: () -> Unit,
    onRefresh: () -> Unit,
    navHost: NavHostController,
    modifier: Modifier = Modifier
) {

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                // Header
                Header(
                    headerText = "My Pets",
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                IconButton(
                    onClick = {
                        navHost.navigate(route = NavigationItem.AddPet.route)
                    },
                    modifier = Modifier.align(Alignment.Bottom)

                ) {
                    Icon(
                        painter = painterResource(Res.drawable.add_2_24px),
                        contentDescription = "Add pet",
                        tint = Color.Blue,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            // Content
            when {
                uiState.isLoading && uiState.pets.isEmpty() -> {
                    LoadingScreen()
                }

                uiState.errorMessage != null && uiState.pets.isEmpty() -> {
                    ErrorScreen(
                        errorMessage = uiState.errorMessage,
                        onClick = onRefresh
                    )
                }

                uiState.pets.isNotEmpty() -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.pets) { pet ->
                            PetCard(pet = pet, navHost = navHost)
                        }
                    }
                }

                else -> {
                    EmptyScreen("No pets found", Res.drawable.sad_cat)

                }
            }
        }
    }

}

// Preview composables
@Preview
@Composable
private fun PetScreenWithPetsPreview() {
    TailyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PetScreenContent(
                uiState = PetUIState(
                    pets = mockPets,
                    isLoading = false,
                    errorMessage = null,
                    isRefreshing = false
                ),
                onLoadPets = {},
                onRefresh = {},
                navHost = rememberNavController(),
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
private fun PetScreenLoadingPreview() {
    TailyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PetScreenContent(
                uiState = PetUIState(
                    pets = emptyList(),
                    isLoading = true,
                    errorMessage = null,
                    isRefreshing = false
                ),
                onLoadPets = {},
                onRefresh = {},
                navHost = rememberNavController(),
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
private fun PetScreenErrorPreview() {
    TailyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PetScreenContent(
                uiState = PetUIState(
                    pets = emptyList(),
                    isLoading = false,
                    errorMessage = "Failed to fetch pets",
                    isRefreshing = false
                ),
                onLoadPets = {},
                onRefresh = {},
                navHost = rememberNavController(),
                modifier = Modifier
            )
        }
    }
}

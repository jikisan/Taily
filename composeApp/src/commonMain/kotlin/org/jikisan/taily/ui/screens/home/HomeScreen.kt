package org.jikisan.taily.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.data.local.mockdata.MockData.mockPets
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.model.pet.Owner
import org.jikisan.taily.domain.model.Weight
import org.jikisan.taily.model.pet.Passport
import org.jikisan.taily.model.pet.Identifiers
import org.jikisan.taily.ui.uistates.HomeUIState
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    modifier: Modifier,
    topPadding: Dp,
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreenContent(
        uiState = uiState,
        onLoadPets = { viewModel.loadPets() },
        onRefresh = { viewModel.refresh() },
        modifier = modifier.padding(top = topPadding)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    uiState: HomeUIState,
    onLoadPets: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Header
        Text(
            text = "My Pets",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Content
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
                    Button(onClick = onLoadPets) {
                        Text("Retry")
                    }
                }
            }

            uiState.pets.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No pets found",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> {
                PullToRefreshBox(
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = onRefresh,
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.pets) { pet ->
                            PetCard(pet = pet)
                        }
                    }
                }
            }
        }
    }
}



// Preview composables
@Preview
@Composable
private fun HomeScreenWithPetsPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreenContent(
                uiState = HomeUIState(
                    pets = mockPets,
                    isLoading = false,
                    errorMessage = null,
                    isRefreshing = false
                ),
                onLoadPets = {},
                onRefresh = {},
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenLoadingPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreenContent(
                uiState = HomeUIState(
                    pets = emptyList(),
                    isLoading = true,
                    errorMessage = null,
                    isRefreshing = false
                ),
                onLoadPets = {},
                onRefresh = {},
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenErrorPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            HomeScreenContent(
                uiState = HomeUIState(
                    pets = emptyList(),
                    isLoading = false,
                    errorMessage = "Failed to fetch pets",
                    isRefreshing = false
                ),
                onLoadPets = {},
                onRefresh = {},
                modifier = Modifier
            )
        }
    }
}




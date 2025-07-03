package org.jikisan.taily.ui.screens.pet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.data.local.mockdata.MockData.mockPets
import org.jikisan.taily.ui.components.Header
import org.jikisan.taily.ui.screens.home.PetCard
import org.jikisan.taily.ui.uistates.PetUIState
import org.koin.compose.viewmodel.koinViewModel

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
        onRefresh = { viewModel.refresh() },
        modifier = modifier.padding(top = topPadding)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PetScreenContent(
    uiState: PetUIState,
    onLoadPets: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Header
        Header("My Pets")

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
private fun PetScreenWithPetsPreview() {
    MaterialTheme {
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
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
private fun PetScreenLoadingPreview() {
    MaterialTheme {
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
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
private fun PetScreenErrorPreview() {
    MaterialTheme {
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
                modifier = Modifier
            )
        }
    }
}

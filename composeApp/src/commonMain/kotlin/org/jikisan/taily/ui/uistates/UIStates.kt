package org.jikisan.taily.ui.uistates

import org.jikisan.taily.domain.model.pet.Pet

data class HomeUIState(
    val pets: List<Pet> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false
)
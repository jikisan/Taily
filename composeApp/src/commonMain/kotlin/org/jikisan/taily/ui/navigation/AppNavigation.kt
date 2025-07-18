package org.jikisan.taily.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.jikisan.taily.ui.screens.addpet.AddPetScreen
import org.jikisan.taily.ui.screens.editpet.EditPetScreen
import org.jikisan.taily.ui.screens.home.HomeScreen
import org.jikisan.taily.ui.screens.pet.PetScreen
import org.jikisan.taily.ui.screens.petdetails.PetDetailsScreen
import org.jikisan.taily.ui.screens.settings.SettingsScreen

@Composable
fun AppNavigation(
    navHostController: NavHostController,
    modifier: Modifier,
    innerPadding: PaddingValues
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val topPadding = innerPadding.calculateTopPadding()

        NavHost(
            navHostController,
            startDestination = NavigationItem.Home.route
        ) {
            composable(NavigationItem.Home.route) {
                HomeScreen(navHostController, modifier, topPadding)
            }
            composable(NavigationItem.Pet.route) {
                PetScreen(navHostController, modifier, topPadding)
            }
            composable(
                route = NavigationItem.PetDetails.route,
            ) { backStackEntry ->
                val petId = backStackEntry.arguments?.getString("petId") ?: ""
                PetDetailsScreen(petId = petId, navHost = navHostController, topPadding)
            }
            composable(
                route = NavigationItem.AddPet.route,
            ) { backStackEntry ->
                AddPetScreen(navHost = navHostController, topPadding = topPadding)
            }
            composable(
                route = NavigationItem.EditPet.route,
            ) { backStackEntry ->
                val petId = backStackEntry.arguments?.getString("petId") ?: ""
                EditPetScreen(petId = petId, navHost = navHostController, topPadding)
            }
            composable(NavigationItem.Settings.route) {
                SettingsScreen(navHostController, modifier, topPadding)
            }
        }
    }
}


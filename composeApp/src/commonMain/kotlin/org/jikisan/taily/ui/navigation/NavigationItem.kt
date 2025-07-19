package org.jikisan.taily.ui.navigation

import org.jetbrains.compose.resources.DrawableResource
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.favorite_24px
import taily.composeapp.generated.resources.home_icon
import taily.composeapp.generated.resources.pets_24px
import taily.composeapp.generated.resources.settings_icon

sealed class NavigationItem(
    val route: String,
    val title: String,
    val icon: DrawableResource? = null,
) {
    data object Home : NavigationItem("home", "Home", Res.drawable.home_icon)
    data object Pet : NavigationItem("pet", "My Pets", Res.drawable.pets_24px)
    data object Settings : NavigationItem("settings", "Settings", Res.drawable.settings_icon)
    data object PetDetails : NavigationItem("petDetails/{petId}", "Pet Details")
    data object AddPet : NavigationItem("addpet/{petId}", "Add Pet")
    data object EditPet : NavigationItem("edit/{petId}", "Edit Pet")
    data object PetIds : NavigationItem("petid/{petId}", "Pet IDs")
    data object PetPassport : NavigationItem("passport/{petId}", "Pet Passport")
    data object PetCare : NavigationItem("care/{petId}", "Pet Care")
    data object PetMedical : NavigationItem("medical/{petId}", "Pet Medical")
}
package org.jikisan.taily.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vidspark.androidapp.ui.theme.FemaleColor
import com.vidspark.androidapp.ui.theme.MaleColor
import org.jetbrains.compose.resources.painterResource
import org.jikisan.taily.domain.model.enum.GenderType
import org.jikisan.taily.domain.model.pet.Pet
import org.jikisan.taily.ui.navigation.NavigationItem
import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.female_24px
import taily.composeapp.generated.resources.male_24px

@Composable
fun GenderIcon(pet: Pet, size: Dp) {
    val resource = when (pet.gender.lowercase()) {
        "male" -> Res.drawable.male_24px
        "female" -> Res.drawable.female_24px
        else -> Res.drawable.male_24px // fallback
    }

    val tint = when (pet.gender.lowercase()) {
        "male" -> MaleColor
        "female" -> FemaleColor
        else -> Color.Gray
    }

    Icon(
        painter = painterResource(resource),
        contentDescription = "Gender Icon",
        tint = tint,
        modifier = Modifier.size(size)
    )
}



@Composable
fun GenderIcon(gender: GenderType, size: Dp) {
    val resource = when (gender) {
        GenderType.Male -> Res.drawable.male_24px
        GenderType.Female -> Res.drawable.female_24px
        // Add other cases here if needed in the future
    }

    val tint = when (gender) {
        GenderType.Male -> MaleColor
        GenderType.Female -> FemaleColor
        // Add other cases here if needed in the future
    }

    Icon(
        modifier = Modifier.size(size),
        painter = painterResource(resource),
        contentDescription = "Gender Icon",
        tint = tint
    )
}
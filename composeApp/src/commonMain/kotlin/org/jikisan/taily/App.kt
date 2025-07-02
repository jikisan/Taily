package org.jikisan.taily

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vidspark.androidapp.ui.theme.Gray
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.ui.navigation.AppNavigation
import org.jikisan.taily.ui.navigation.BottomNavBar
import org.jikisan.taily.ui.navigation.NavigationItem

import taily.composeapp.generated.resources.Res
import taily.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    TailyTheme {
        val navHostController = rememberNavController()

        Scaffold(
            bottomBar = { BottomNavBar(
                navHostController = navHostController,
                bottomNavItems = topLevelDestinations,
                modifier = Modifier
            ) }
        ) { innerPadding ->
            AppNavigation(
                navHostController = navHostController,
                modifier = Modifier,
                innerPadding = innerPadding
            )
        }
    }
}

val topLevelDestinations = listOf(
    NavigationItem.Home,
    NavigationItem.Pet,
    NavigationItem.Settings,
)


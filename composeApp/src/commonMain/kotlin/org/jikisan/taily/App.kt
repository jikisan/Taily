package org.jikisan.taily

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jikisan.taily.ui.navigation.AppNavigation
import org.jikisan.taily.ui.navigation.BottomNavBar
import org.jikisan.taily.ui.navigation.NavigationItem

@Composable
@Preview
fun App() {
    TailyTheme {
        val navHostController = rememberNavController()
        val isTopLevelDestination =
            navHostController.currentBackStackEntryAsState().value?.destination?.route in topLevelDestinations.map { it.route }
        val isScrollingDown = remember { mutableStateOf(false) }

        // Animate bottom bar translation (slide down/up)
        val bottomBarTranslationY by animateFloatAsState(
            targetValue = if (isScrollingDown.value) 240f else 0f, // Positive value slides down
            animationSpec = tween(durationMillis = 300),
            label = "BottomBarTranslation"
        )

        val scrollThreshold = 10f
        val accumulatedScroll = remember { mutableStateOf(0f) }

        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    // Only respond to vertical scrolls
                    if (available.y != 0f) {
                        accumulatedScroll.value += available.y

                        // Update state only when threshold is exceeded
                        if (accumulatedScroll.value < -scrollThreshold) {
                            isScrollingDown.value = true
                            accumulatedScroll.value = 0f
                        } else if (accumulatedScroll.value > scrollThreshold) {
                            isScrollingDown.value = false
                            accumulatedScroll.value = 0f
                        }
                    }
                    return Offset.Zero
                }
            }
        }

        Scaffold(
            bottomBar = {
                if (isTopLevelDestination) {
                    BottomNavBar(
                        navHostController = navHostController,
                        bottomNavItems = topLevelDestinations,
                        modifier = Modifier.graphicsLayer {
                            // Apply translation animation (slide down/up)
                            translationY = bottomBarTranslationY
                        }
                    )
                }
            }
        ) { innerPadding ->
            AppNavigation(
                navHostController = navHostController,
                modifier = Modifier.nestedScroll(nestedScrollConnection),
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


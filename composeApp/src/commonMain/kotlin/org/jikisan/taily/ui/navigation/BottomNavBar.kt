package org.jikisan.taily.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vidspark.androidapp.ui.theme.Gray
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BottomNavBar(
    navHostController: NavHostController,
    bottomNavItems: List<NavigationItem>,
    modifier: Modifier
) {
    NavigationBar( modifier.fillMaxWidth() ) {
        bottomNavItems.forEach { item ->

            val currentDestination = navHostController.currentBackStackEntryAsState().value?.destination?.route
            val isSelected = item.route == currentDestination

            NavigationBarItem(
                icon = {
                    item.icon?.let { icon ->
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = item.title
                        )
                    }
                },
                label = { Text(
                    text = item.title,
                    style = MaterialTheme.typography.labelSmall,
//                    color = NavigationBarItemDefaults.colors(
//                        selectedIconColor = MaterialTheme.colorScheme.primary,
//                        unselectedIconColor = Gray
//                    ),
                    fontSize = 12.sp
                ) },
                selected = isSelected,
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Gray,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = Gray
                ),
                onClick = {
                    if (item.route != currentDestination) {
                        navHostController.navigate(route = item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            navHostController.graph.findStartDestination().route?.let {
                                popUpTo(it) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }

    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    val navController = rememberNavController()
    val navItems = listOf(
        NavigationItem.Home,
        NavigationItem.Pet,
        NavigationItem.Settings
    )

    MaterialTheme {
        BottomNavBar(
            navHostController = navController,
            bottomNavItems = navItems,
            modifier = Modifier.wrapContentHeight()
        )
    }
}
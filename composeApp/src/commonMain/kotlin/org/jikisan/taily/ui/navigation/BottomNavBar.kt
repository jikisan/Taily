package org.jikisan.taily.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vidspark.androidapp.ui.theme.Gray
import com.vidspark.androidapp.ui.theme.TailyTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BottomNavBar(
    navHostController: NavHostController,
    bottomNavItems: List<NavigationItem>,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(35.dp),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
                .clip(RoundedCornerShape(35.dp)),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
        ) {
            NavigationBar(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                containerColor = Color.Transparent,
                tonalElevation = 0.dp,
            ) {
                bottomNavItems.forEach { item ->
                    val currentDestination =
                        navHostController.currentBackStackEntryAsState().value?.destination?.route
                    val isSelected = item.route == currentDestination

                    // Animation states
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.1f else 1f,
                        animationSpec = spring(
                            dampingRatio = 0.6f,
                            stiffness = 300f
                        ),
                        label = "scale"
                    )

                    val iconColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Gray,
                        animationSpec = tween(durationMillis = 300),
                        label = "iconColor"
                    )

                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Gray,
                        animationSpec = tween(durationMillis = 300),
                        label = "textColor"
                    )

                    NavigationBarItem(
                        icon = {
                            Box(
                                modifier = Modifier
                                    .scale(scale)
                                    .size(28.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                // Background circle for selected item
//                                if (isSelected) {
//                                    Surface(
//                                        modifier = Modifier
//                                            .size(40.dp)
//                                            .clip(RoundedCornerShape(20.dp)),
//                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
//                                    ) {}
//                                }

                                item.icon?.let { icon ->
                                    Icon(
                                        painter = painterResource(icon),
                                        contentDescription = item.title,
                                        tint = iconColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        },
                        label = {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = if (isSelected) 13.sp else 12.sp,
                                color = textColor
                            )
                        },
                        selected = isSelected,
                        alwaysShowLabel = false,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Transparent,
                            unselectedIconColor = Color.Transparent,
                            selectedTextColor = Color.Transparent,
                            unselectedTextColor = Color.Transparent,
                            indicatorColor = Color.Transparent
                        ),
                        onClick = {
                            if (item.route != currentDestination) {
                                navHostController.navigate(route = item.route) {
                                    navHostController.graph.findStartDestination().route?.let {
                                        popUpTo(it) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
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

    TailyTheme {
        BottomNavBar(
            navHostController = navController,
            bottomNavItems = navItems,
            modifier = Modifier.wrapContentHeight()
        )
    }
}
package org.jikisan.taily.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navHostController: NavHostController, modifier: Modifier, topPadding: Dp) {

    Box( modifier = modifier.fillMaxSize() ) {
        Text( text = "Home", modifier = Modifier.align(Alignment.Center) )
    }
}
package org.jikisan.taily.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.animateFloatAsState
import kotlin.math.abs
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import kotlin.math.roundToInt
import kotlinx.coroutines.delay

// Define snackbar type
enum class SnackbarType {
    ERROR, WARNING, INFO, SUCCESS
}

// Helper function for colors/icons per type
private fun snackbarColors(type: SnackbarType): Pair<List<Color>, Color> = when (type) {
    SnackbarType.ERROR -> listOf(
        Color(0xFFF85032),
        Color(0xFFE73827)
    ) to Color.White // red-gradient
    SnackbarType.WARNING -> listOf(
        Color(0xFFFEC163),
        Color(0xFFDE4313)
    ) to Color.Black // orange-gradient
    SnackbarType.INFO -> listOf(
        Color(0xFF2193b0),
        Color(0xFF6dd5ed)
    ) to Color.White // blue-gradient
    SnackbarType.SUCCESS -> listOf(
        Color(0xFF56ab2f),
        Color(0xFFa8e063)
    ) to Color.White // green-gradient
}

@Composable
fun GradientSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    type: SnackbarType = SnackbarType.INFO,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    val (gradientColors, contentColor) = snackbarColors(type)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp)
            .wrapContentHeight(align = Alignment.Top)
            .background(
                brush = Brush.horizontalGradient(gradientColors),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Icon: you can use real icons or add resources later
            val icon = when (type) {
                SnackbarType.ERROR -> "\u26A0" // ⚠
                SnackbarType.WARNING -> "\u26A0" // ⚠
                SnackbarType.INFO -> "\u2139" // ℹ
                SnackbarType.SUCCESS -> "\u2714" // ✔
            }
            Text(
                icon,
                fontSize = 20.sp,
                color = contentColor,
                modifier = Modifier.padding(end = 10.dp)
            )
            Text(
                message,
                color = contentColor,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            if (actionLabel != null && onAction != null) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    actionLabel,
                    color = contentColor.copy(alpha = 0.85f),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onAction)
                )
            }
        }
    }
}

/**
 * Duration options for Snackbar
 */
enum class SnackbarDuration(val millis: Long) {
    SHORT(2000),
    LONG(5000)
}

/**
 * Host Composable for GradientSnackbar.
 * Usage example (call showSnackbar from parent):
 * val snackbarHostState = remember { SnackbarHostState() }
 * snackbarHostState.showSnackbar("Message", SnackbarType.ERROR, SnackbarDuration.SHORT)
 */
@Composable
fun AnimatedGradientSnackbarHost(
    message: String?,
    onDismiss: () -> Unit,
    type: SnackbarType = SnackbarType.INFO,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    duration: SnackbarDuration = SnackbarDuration.SHORT
) {
    val density = LocalDensity.current
    val threshold = with(density) { 100.dp.toPx() }

    var offsetX by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var timerVersion by remember { mutableStateOf(0) }


    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = if (isDragging) {
            // No animation while dragging
            tween(0)
        } else {
            // Spring back animation when released
            spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        },
        label = "snackbarOffset"
    )

    // Reset offset when new message appears
    LaunchedEffect(message) {
        if (message != null) {
            offsetX = 0f
            timerVersion++
        }
    }

    // Auto-dismiss timer
    LaunchedEffect(timerVersion) {
        if (message != null) {
            delay(duration.millis)
            onDismiss()
        }
    }

    // Handle swipe dismiss
    LaunchedEffect(animatedOffsetX) {
        if (!isDragging && abs(animatedOffsetX) > threshold) {
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = message != null,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    ) {
        Box(
            Modifier
                .offset { IntOffset(animatedOffsetX.toInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX += delta
                    },
                    onDragStarted = {
                        isDragging = true
                    },
                    onDragStopped = {
                        isDragging = false
                        if (abs(offsetX) > threshold) {
                            // Don't reset offset, let it dismiss
                        } else {
                            // Spring back to center and reset timer
                            offsetX = 0f
                            timerVersion++
                        }
                    }
                )
        ) {
            GradientSnackbar(
                message = message ?: "",
                type = type,
                actionLabel = actionLabel,
                onAction = onAction,
                modifier = Modifier
            )
        }
    }
}

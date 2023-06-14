package com.yewo.tictactoe

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/**
 * An empty cell in the grid
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Empty(size: Dp = 100.dp, padding: Dp = 14.dp, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .padding(padding)
            .size(size)
            .alpha(0f)
    ) { }
}

/**
 * An O
 */
@Composable
fun Nought(
    size: Dp = 100.dp,
    color: Color = Color(0xFF00CCFF),
    strokeWidth: Dp = 14.dp,
) {
    var startedAnimation by remember {
        mutableStateOf(false)
    }

    val sweepAngle by animateFloatAsState(
        targetValue = if (startedAnimation) -360f else 0f,
        animationSpec = tween(durationMillis = 1000),
    )

    LaunchedEffect(true) {
        startedAnimation = true
    }

    val stroke = with(LocalDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
    }

    Canvas(
        modifier = Modifier
            .padding(strokeWidth)
            .size(size)
    ) {
        drawArc(
            color,
            startAngle = -60f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke
        )
    }
}

/**
 * An X
 */
@Composable
fun Cross(
    size: Dp = 100.dp,
    color: Color = Color(0xFF00FF88),
    strokeWidth: Dp = 14.dp,
) {
    var startedAnimation by remember { mutableStateOf(false) }

    val animatedFloat by animateFloatAsState(
        targetValue = if (startedAnimation) 2f else 0f,
        animationSpec = tween(durationMillis = 1000),
    )

    val firstLineAnimatedFloat: Float = if (animatedFloat > 1) 1f else animatedFloat
    val secondLineAnimatedFloat: Float = if (animatedFloat < 1) 0f else animatedFloat - 1f

    LaunchedEffect(true) {
        startedAnimation = true
    }

    Canvas(
        modifier = Modifier
            .padding(strokeWidth)
            .size(size)
    ) {
        val padding = strokeWidth.toPx() / 2
        val sizeMinusPadding = size.toPx() - padding
        val sizeMinus2Padding = size.toPx() - 2 * padding

        // First line
        drawLine(
            color,
            Offset(
                sizeMinusPadding,
                padding
            ),
            Offset(
                sizeMinusPadding - sizeMinus2Padding * firstLineAnimatedFloat,
                sizeMinus2Padding * firstLineAnimatedFloat + padding
            ),
            strokeWidth.toPx(),
            cap = StrokeCap.Round,
        )

        // Second line
        if (secondLineAnimatedFloat > 0) {
            drawLine(
                color,
                Offset(
                    padding,
                    padding
                ),
                Offset(
                    sizeMinusPadding * secondLineAnimatedFloat,
                    sizeMinusPadding * secondLineAnimatedFloat
                ),
                strokeWidth.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}
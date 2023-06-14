package com.yewo.tictactoe

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * Displays a line over the cells belonging
 * to the winner which form a line
 */
@Composable
fun WinningLine(
    screenWidth: Int,
    start: Coordinate,
    end: Coordinate,
    strokeWidth: Dp
) {
    var startedAnimation by remember { mutableStateOf(false) }

    val animatedFloat by animateFloatAsState(
        targetValue = if (startedAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
    )

    LaunchedEffect(true) {
        Handler(
            Looper.getMainLooper()
        ).postDelayed(
            { startedAnimation = true },
            1000
        )
    }

    Canvas(modifier = Modifier.size(screenWidth.dp)) {
        val aThirdOfScreen = screenWidth.dp.toPx() / 3

        val startX = (aThirdOfScreen / 2) + start.x.toFloat() * aThirdOfScreen
        val startY = (aThirdOfScreen / 2) + start.y.toFloat() * aThirdOfScreen
        val endX = (aThirdOfScreen / 2) + end.x.toFloat() * aThirdOfScreen
        val endY = (aThirdOfScreen / 2) + end.y.toFloat() * aThirdOfScreen

        if (animatedFloat > 0) {
            drawLine(
                Color.Black,
                Offset(
                    startX,
                    startY,
                ),
                Offset(
                    startX + (endX - startX) * animatedFloat,
                    startY + (endY - startY) * animatedFloat,
                ),
                strokeWidth.toPx() * 1.3f,
                cap = StrokeCap.Round,
            )
        }
    }
}

/**
 * Displays the grid of cells which include noughts (O's),
 * crosses (X's), and empty cells which can be clicked on
 */
@Composable
fun Boxes(
    gameState: List<GameElement>,
    boxSize: Dp = 100.dp,
    strokeWidth: Dp = 14.dp,
    clickedOnBox: (index: Int) -> Unit,
) {
    Column {
        for (row in 0..2) {
            Row {
                for (column in 0..2) {
                    val index = row * 3 + column

                    Box(
                        modifier = Modifier.padding(strokeWidth)
                    ) {
                        when (gameState[index]) {
                            GameElement.Empty -> {
                                Empty(
                                    onClick = { clickedOnBox(index) },
                                    size = boxSize,
                                    padding = strokeWidth,
                                )
                            }

                            GameElement.Nought -> {
                                Nought(
                                    size = boxSize,
                                    strokeWidth = strokeWidth,
                                )
                            }

                            GameElement.Cross -> {
                                Cross(
                                    size = boxSize,
                                    strokeWidth = strokeWidth,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Lines which form the grid borders
 */
@Composable
fun BackgroundBorderLines(
    size: Dp,
    strokeWidth: Dp,
    color: Color = Color.White
) {
    var startedAnimation by remember { mutableStateOf(false) }

    val animatedFloat by animateFloatAsState(
        targetValue = if (startedAnimation) 4f else 0f,
        animationSpec = tween(durationMillis = 2000),
    )

    val animatedFloatSubRange = { start: Float, end: Float ->
        if (animatedFloat < start) {
            0f
        } else if (animatedFloat > end) {
            1f
        } else {
            animatedFloat - start
        }
    }

    val firstLineAnimatedFloat = animatedFloatSubRange(0f, 1f)
    val secondLineAnimatedFloat = animatedFloatSubRange(1f, 2f)
    val thirdLineAnimatedFloat = animatedFloatSubRange(2f, 3f)
    val fourthLineAnimatedFloat = animatedFloatSubRange(3f, 4f)

    LaunchedEffect(true) {
        startedAnimation = true
    }

    Canvas(
        modifier = Modifier.size(size)
    ) {
        val aThirdOfSize = size.toPx() / 3
        val thirtyPercentStroke = strokeWidth.toPx() * 0.3f

        // First vertical line
        drawLine(
            color,
            Offset(
                aThirdOfSize - thirtyPercentStroke,
                strokeWidth.toPx(),
            ),
            Offset(
                aThirdOfSize - thirtyPercentStroke,
                size.toPx() * firstLineAnimatedFloat - strokeWidth.toPx()
            ),
            strokeWidth.toPx(),
            cap = StrokeCap.Round,
        )

        // Second vertical line
        if (secondLineAnimatedFloat > 0) {
            drawLine(
                color,
                Offset(
                    2 * aThirdOfSize - thirtyPercentStroke,
                    strokeWidth.toPx(),
                ),
                Offset(
                    2 * aThirdOfSize - thirtyPercentStroke,
                    size.toPx() * secondLineAnimatedFloat - strokeWidth.toPx()
                ),
                strokeWidth.toPx(),
                cap = StrokeCap.Round,
            )
        }

        // First horizontal line
        if (thirdLineAnimatedFloat > 0) {
            drawLine(
                color,
                Offset(
                    strokeWidth.toPx(),
                    thirtyPercentStroke + aThirdOfSize,
                ),
                Offset(
                    size.toPx() * thirdLineAnimatedFloat - strokeWidth.toPx(),
                    thirtyPercentStroke + aThirdOfSize,
                ),
                strokeWidth.toPx(),
                cap = StrokeCap.Round,
            )
        }

        // Second horizontal line
        if (fourthLineAnimatedFloat > 0) {
            drawLine(
                color,
                Offset(
                    strokeWidth.toPx(),
                    2 * aThirdOfSize - thirtyPercentStroke,
                ),
                Offset(
                    size.toPx() * fourthLineAnimatedFloat - strokeWidth.toPx(),
                    2 * aThirdOfSize - thirtyPercentStroke,
                ),
                strokeWidth.toPx(),
                cap = StrokeCap.Round,
            )
        }
    }
}
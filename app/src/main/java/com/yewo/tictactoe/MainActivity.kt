package com.yewo.tictactoe

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yewo.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            TicTacToeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameContainer()
                }
            }
        }
    }
}

/**
 * A data element which represents what occupies a cell in the game
 */
enum class GameElement {
    Empty,
    Nought,
    Cross,
}

fun newGameState() = listOf(
    GameElement.Empty,
    GameElement.Empty,
    GameElement.Empty,
    GameElement.Empty,
    GameElement.Empty,
    GameElement.Empty,
    GameElement.Empty,
    GameElement.Empty,
    GameElement.Empty,
)

/**
 * Returns a new version of a list with the element
 * at the given `index` replaced by `newValue`
 */
fun <T> List<T>.setElementImmutably(index: Int, newValue: T): List<T> {
    return mapIndexed { i, element ->
        if (i == index) newValue else element
    }
}

@Composable
fun GameContainer() {
    var gameState by remember { mutableStateOf(newGameState()) }
    var isTurnOfCrosses by remember { mutableStateOf(false) }
    val winner = determineWinner(gameState)

    if (winner.winner != GameElement.Empty) {
        LaunchedEffect(key1 = winner) {
            Handler(
                Looper.getMainLooper()
            ).postDelayed(
                { gameState = newGameState() },
                2500
            )
        }
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val strokeWidth = (screenWidth * 0.03).dp
    val boxSize = ((screenWidth - screenWidth * 0.045 * 8) / 3).dp

    val clickedOnBox = { index: Int ->
        if (winner.winner == GameElement.Empty) {
            gameState = gameState.setElementImmutably(
                index,
                newValue = if (isTurnOfCrosses) {
                    GameElement.Cross
                } else {
                    GameElement.Nought
                }
            )
            isTurnOfCrosses = !isTurnOfCrosses
        }
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFE2701A))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(screenWidth.dp)
        ) {
            BackgroundBorderLines(
                size = screenWidth.dp,
                strokeWidth = strokeWidth,
            )
            Boxes(
                boxSize = boxSize,
                strokeWidth = strokeWidth,
                gameState = gameState,
                clickedOnBox = clickedOnBox
            )
            if (winner.winningLine != null) {
                WinningLine(
                    screenWidth,
                    start = winner.winningLine.start,
                    end = winner.winningLine.end,
                    strokeWidth
                )
            }
        }
        Button(
            onClick = { gameState = newGameState() },
        ) {
            Text(
                text = "Restart",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(2.dp),
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GamePreview() {
    TicTacToeTheme {
        GameContainer()
    }
}


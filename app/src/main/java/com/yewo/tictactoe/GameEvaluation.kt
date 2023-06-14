package com.yewo.tictactoe

data class Coordinate(val x: Int, val y: Int)
data class LineCoordinates(val start: Coordinate, val end: Coordinate)

/**
 * The result of trying to determine whether there was a winner
 */
data class WinEvaluation(
    val winner: GameElement,
    val winningLine: LineCoordinates?,
)

/**
 * Determines if either of the players has won and, if so,
 * the coordinates of the line with cells which have won
 */
fun determineWinner(gameState: List<GameElement>): WinEvaluation {
    var winner = GameElement.Empty
    var winningLine: LineCoordinates? = null

    for (i in 0..2) {
        // Search by column
        if (
            gameState[i] == gameState[i + 3] &&
            gameState[i + 3] == gameState[i + 6] &&
            gameState[i] != GameElement.Empty
        ) {
            winner = gameState[i]
            winningLine = LineCoordinates(
                Coordinate(i, 0),
                Coordinate(i, 2)
            )
            // Search by row
        } else if (
            gameState[i * 3] == gameState[i * 3 + 1] &&
            gameState[i * 3 + 1] == gameState[i * 3 + 2] &&
            gameState[i * 3] != GameElement.Empty
        ) {
            winner = gameState[i * 3]
            winningLine = LineCoordinates(
                Coordinate(0, i),
                Coordinate(2, i)
            )
        }
    }
    // Search by diagonals
    if (
        gameState[0] == gameState[4] &&
        gameState[4] == gameState[8] &&
        gameState[0] != GameElement.Empty
    ) {
        winner = gameState[0]
        winningLine = LineCoordinates(
            Coordinate(0, 0),
            Coordinate(2, 2)
        )
    } else if (
        gameState[2] == gameState[4] &&
        gameState[4] == gameState[6] &&
        gameState[2] != GameElement.Empty
    ) {
        winner = gameState[2]
        winningLine = LineCoordinates(
            Coordinate(2, 0),
            Coordinate(0, 2)
        )
    }

    return WinEvaluation(winner, winningLine)
}
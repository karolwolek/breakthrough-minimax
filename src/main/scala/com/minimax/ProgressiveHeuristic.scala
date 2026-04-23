package com.minimax

object ProgressiveHeuristic extends Heuristic[BreakthroughState] {
  override def apply(s: BreakthroughState): Double = {
    def calculatePlayerScore(positions: Set[Int], isWhite: Boolean): Double = {
      positions.map { pos =>
        // we count rows from 0
        val row = (pos - 1) / s.boardSize
        val progress = if (isWhite) row else (s.boardSize - 1) - row

        // 1, 2, 4, 8, 16, 32, 64, 128
        math.pow(2.0, progress)
      }.sum
    }

    val whiteEval = calculatePlayerScore(s.whitePositions, true)
    val blackEval = calculatePlayerScore(s.blackPositions, false)

    if (s.isPlayerOneTurn) {
      if (s.playerOneWin) return 100000.0
      if (s.playerTwoWin) return -100000.0
      whiteEval - blackEval
    } else {
      if (s.playerOneWin) return -100000.0
      if (s.playerTwoWin) return 100000.0
      blackEval - whiteEval
    }
  }
}

package com.minimax

object BalancedLeadHeuristic extends Heuristic[BreakthroughState] {

  override def apply(s: BreakthroughState): Double = {

    def evaluate(positions: Set[Int], isWhite: Boolean): Double = {
      if (positions.isEmpty) return 0.0

      val scores = positions.toList.map { pos =>
        val row = (pos - 1) / s.boardSize
        val progress = if (isWhite) row else (s.boardSize - 1) - row

        // 10 for pawn and + 2 for every row
        10.0 + (progress * 2.0)
      }
      scores.sum + (scores.max * 5.0)
    }

    val whiteEval = evaluate(s.whitePositions, true)
    val blackEval = evaluate(s.blackPositions, false)

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

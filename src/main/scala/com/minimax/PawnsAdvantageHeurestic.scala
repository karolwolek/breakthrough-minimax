package com.minimax

object PawnsAdvantageHeurestic extends Heurestic[BreakthroughState] {

  override def apply(s: BreakthroughState): Double =
    if (s.isPlayerOneTurn) {
      if (s.playerOneWin) return 100000.0
      if (s.playerTwoWin) return -100000.0

      (s.whitePositions.size - s.blackPositions.size).toDouble
    } else {
      if (s.playerTwoWin) return 100000.0
      if (s.playerOneWin) return -100000.0

      (s.blackPositions.size - s.whitePositions.size).toDouble
    }
}

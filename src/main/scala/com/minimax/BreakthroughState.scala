package com.minimax

class BreakthroughState(
    val boardSize: Int = 8,
    val blackPositions: Set[Int],
    val whitePositions: Set[Int],
    val isPlayerOneTurn: Boolean
) extends State[BreakthroughState] {

  override def isGameOver: Boolean = playerOneWin || playerTwoWin

  override def playerOneWin: Boolean = ???

  override def playerTwoWin: Boolean = ???

  override def generateStates: Seq[BreakthroughState] = ???

  lazy val allPositions = math.pow(boardSize, 2)

  def makeMove(pos: Int): Option[BreakthroughState] = {
    if (pos > allPositions || pos < 1) then None
    else if (isPlayerOneTurn) then ???
    else ???
  }

  def movesForPawn(pos: Int): Option[List[Int]] =
    if (isPlayerOneTurn) then possibleMoves(whitePositions, blackPositions, pos)
    else possibleMoves(blackPositions, whitePositions, pos)

  private def possibleMoves(
      playerPawns: Set[Int],
      enemyPawns: Set[Int],
      pos: Int
  ): Option[List[Int]] =
    playerPawns.find(_ == pos) match {
      case Some(value) =>
        Some(
          (for
            move <- 7 to 9
            // can't move left diagonal on left edge
            if (move == 7 && pos + move % boardSize != 0)
            // can't move rigth diagonal on right edge
            if (move == 9 && pos + move % boardSize != 1)
            // can't move straight, if enemy is on place
            if (move == 8 && !enemyPawns.contains(pos + move))
          yield pos + move).toList
        )
      case None => None
    }

}

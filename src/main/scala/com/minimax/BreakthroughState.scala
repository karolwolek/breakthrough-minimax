package com.minimax

case class Move(currentPos: Int, newPos: Int, direction: Direction)

enum Direction:
  case LeftDiagonal, Straight, RightDiagonal

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

  def makeMove(move: Move): Option[BreakthroughState] = {
    if (pos > allPositions || pos < 1) then None
    else if (isPlayerOneTurn) then ???
    else ???
  }

  def movesForPawn(pos: Int): Option[List[Move]] =
    if (isPlayerOneTurn) then possibleMoves(whitePositions, blackPositions, pos)
    else possibleMoves(blackPositions, whitePositions, pos)

  private def possibleMoves(
      playerPawns: Set[Int],
      enemyPawns: Set[Int],
      pos: Int
  ): Option[List[Move]] =
    playerPawns.find(_ == pos) match {
      case Some(value) =>
        Some(
          (for
            move <- (7 to 9).zip(Direction.values)
            // can't move left diagonal on left edge
            if (move._1 == 7 && pos + move._1 % boardSize != 0)
            // can't move rigth diagonal on right edge
            if (move._1 == 9 && pos + move._1 % boardSize != 1)
            // can't move straight, if enemy is on place
            if (move._1 == 8 && !enemyPawns.contains(pos + move._1))
          yield Move(pos, pos + move._1, move._2)).toList
        )
      case None => None
    }

}

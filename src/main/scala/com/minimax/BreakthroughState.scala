package com.minimax

import com.minimax.BreakthroughState.initBlacks
import com.minimax.BreakthroughState.initWhites

case class Move(currentPos: Int, newPos: Int, direction: Direction)

enum Direction:
  case LeftDiagonal, Straight, RightDiagonal

enum PawnOwner:
  case PlayerOne, PlayerTwo, Empty, LastMove

class BreakthroughState(
    val boardSize: Int = 8,
    val blackPositions: Set[Int],
    val whitePositions: Set[Int],
    val isPlayerOneTurn: Boolean,
    private val lastMovePos: Option[Int]
) extends State[BreakthroughState] {

  def this(boardSize: Int, whitePositions: Set[Int], blackPositions: Set[Int]) =
    this(boardSize, blackPositions, whitePositions, true, None)

  def this(boardSize: Int) =
    this(boardSize, initBlacks(boardSize), initWhites(boardSize), true, None)

  override def isGameOver: Boolean = playerOneWin || playerTwoWin

  lazy val playerOneWin: Boolean =
    blackPositions.isEmpty || isWhiteOnBlack()

  lazy val playerTwoWin: Boolean =
    whitePositions.isEmpty || isBlackOnWhite()

  lazy val allPositions = math.pow(boardSize, 2)

  override def generateStates: Seq[BreakthroughState] =
    val playerPositions =
      if isPlayerOneTurn then whitePositions else blackPositions
    playerPositions.flatMap { pawn =>
      movesForPawn(pawn) match {
        case Some(moves) => moves.flatMap(makeMove(_))
        case None        => None
      }
    }.toSeq

  def makeMove(move: Move): Option[BreakthroughState] = {
    // TODO: check if below condition is needed
    if (move.newPos > allPositions || move.newPos < 1) then None
    else if (isPlayerOneTurn) then
      Some(
        BreakthroughState(
          boardSize,
          blackPositions - move.newPos,
          whitePositions - move.currentPos + move.newPos,
          !isPlayerOneTurn,
          Some(move.currentPos)
        )
      )
    else
      Some(
        BreakthroughState(
          boardSize,
          blackPositions - move.currentPos + move.newPos,
          whitePositions - move.newPos,
          !isPlayerOneTurn,
          Some(move.currentPos)
        )
      )
  }

  def movesForPawn(pos: Int): Option[List[Move]] =
    if (isPlayerOneTurn) then possibleMoves(whitePositions, blackPositions, pos)
    else possibleMoves(blackPositions, whitePositions, pos)

  def pawnOwner(pos: Int): PawnOwner =
    if whitePositions.contains(pos) then PawnOwner.PlayerOne
    else if blackPositions.contains(pos) then PawnOwner.PlayerTwo
    else
      lastMovePos match {
        case Some(value) =>
          if value == pos then PawnOwner.LastMove else PawnOwner.Empty
        case None => PawnOwner.Empty

      }

  private def possibleMoves(
      playerPawns: Set[Int],
      enemyPawns: Set[Int],
      pos: Int
  ): Option[List[Move]] =
    Option
      .when(playerPawns.contains(pos)) {
        val step = if isPlayerOneTurn then 1 else -1

        (boardSize - 1 to boardSize + 1)
          .zip(Direction.values)
          .flatMap { (offset, dir) =>
            val target = pos + (offset * step)
            val col = target % boardSize // zero is the 8th column

            val isPathClear = offset match {
              case x if x == boardSize - 1 =>
                col != (if isPlayerOneTurn then 0 else 1)
              case x if x == boardSize     => !enemyPawns.contains(target)
              case x if x == boardSize + 1 =>
                col != (if isPlayerOneTurn then 1 else 0)
              case _ => false // Always include a default case
            }

            Option.when(isPathClear && !playerPawns.contains(target)) {
              Move(pos, target, dir)
            }
          }
          .toList

      }

  // check if blacks are on white 1 to n positions
  private def isBlackOnWhite(): Boolean =
    1 to boardSize exists (blackPositions
      .contains(_))

  // check if whites are on black boardsize - n to boardSize positions
  private def isWhiteOnBlack(): Boolean =
    (allPositions.toInt - boardSize) to allPositions.toInt exists (whitePositions
      .contains(_))

}

object BreakthroughState {

  def initWhites(boardSize: Int): Set[Int] =
    (1 to 2 * boardSize).toSet

  def initBlacks(boardSize: Int): Set[Int] =
    val offset = (math.pow(boardSize, 2) - 2 * boardSize).toInt
    initWhites(boardSize).map(_ + offset)

  def offset(row: Int, column: Int, boardSize: Int) =
    (row - 1) * boardSize + column

}

package com.minimax

import scala.io.Source
import scala.util.{Try, Using}

class Breakthrough(
    playerOne: Player[BreakthroughState],
    playerTwo: Player[BreakthroughState],
    var game: BreakthroughState,
    boardSize: Int
) {

  // constructor without initial state
  def this(
      playerOne: Player[BreakthroughState],
      playerTwo: Player[BreakthroughState],
      boardSize: Int
  ) = this(
    playerOne,
    playerTwo,
    new BreakthroughState(boardSize),
    boardSize
  )

  def playVerbose() =
    println("Rozpoczynamy grę w breakthrough!")
    println(display(game))

    val startTime = System.nanoTime()
    var rounds = 0

    while (!game.isGameOver) {
      rounds += 1
      if game.isPlayerOneTurn then {
        println("White to move")
        game = playerOne.move(game)
      } else {
        println("Black to move")
        game = playerTwo.move(game)
      }
      println(s"${display(game)}\n")
    }

    val endTime = System.nanoTime()

    if (game.playerOneWin) println("Grę wygrywa gracz pierwszy")
    else println("Grę wygrywa gracz drugi")
    println(s"Liczba rund: $rounds")

    val timeInSeconds = (endTime - startTime) / 1_000_000_000.0
    System.err.println(
      s"Odwiedzone węzły gracza 1: ${playerOne.nodesVisited()}"
    )
    System.err.println(
      s"Odwiedzone węzły gracza 2: ${playerTwo.nodesVisited()}"
    )
    System.err.println(f"Czas działania algorytmu: $timeInSeconds%.4f s")

  def playWithResult(
      depth: Int,
      h1Name: String,
      h2Name: String
  ): GameSummary = {
    val startTime = System.nanoTime()
    var rounds = 0

    while (!game.isGameOver) {
      rounds += 1
      if (game.isPlayerOneTurn) {
        game = playerOne.move(game)
      } else {
        game = playerTwo.move(game)
      }
    }

    val endTime = System.nanoTime()

    GameSummary(
      winner = if (game.playerOneWin) 1 else 2,
      rounds = rounds,
      timeTakenNanos = endTime - startTime,
      nodesP1 = playerOne.nodesVisited(),
      nodesP2 = playerTwo.nodesVisited(),
      depth = depth,
      boardSize = game.boardSize,
      heuristicP1 = h1Name,
      heuristicP2 = h2Name,
      finalState = game
    )
  }

  def display(state: BreakthroughState): String = {
    (boardSize to 1 by -1)
      .map(row => {
        (1 to boardSize)
          .map(column => {
            state.pawnOwner(
              BreakthroughState.offset(row, column, boardSize)
            ) match {
              case PawnOwner.Empty     => Breakthrough.EMPTY_CELL
              case PawnOwner.LastMove  => Breakthrough.LAST_MOVE
              case PawnOwner.PlayerOne => Breakthrough.PLAYER_ONE
              case PawnOwner.PlayerTwo => Breakthrough.PLAYER_TWO
            }

          })
          .mkString(" ")
      })
      .mkString("\n")

  }

}

object Breakthrough {

  final val PLAYER_ONE = "W"
  final val PLAYER_TWO = "B"
  final val EMPTY_CELL = "_"
  final val LAST_MOVE = "o"

  def parse(
      input: String,
      isPlayerOneTurn: Boolean = true
  ): BreakthroughState = {
    val lines = input.trim.split("\n").map(_.trim.split("\\s+"))
    val boardSize = lines.length

    var whitePositions = Set.empty[Int]
    var blackPositions = Set.empty[Int]
    var lastMovePos: Option[Int] = None

    // Iterate through lines (which represent row boardSize down to 1)
    for {
      (rowTokens, lineIndex) <- lines.zipWithIndex
      row = boardSize - lineIndex // Mapping top line to highest row number
      (token, colIndex) <- rowTokens.zipWithIndex
      column = colIndex + 1
      pos = BreakthroughState.offset(row, column, boardSize)
    } {
      token match {
        case PLAYER_ONE => whitePositions += pos
        case PLAYER_TWO => blackPositions += pos
        case LAST_MOVE  => lastMovePos = Some(pos)
        case _          => // Ignore EMPTY_CELL
      }
    }

    new BreakthroughState(
      boardSize = boardSize,
      blackPositions = blackPositions,
      whitePositions = whitePositions,
      isPlayerOneTurn = isPlayerOneTurn,
      lastMovePos = lastMovePos
    )
  }

  def readStateFromFile(
      path: String,
      isPlayerOneTurn: Boolean
  ): Try[BreakthroughState] = {
    Using(Source.fromFile(path)) { source =>
      val content = source.mkString
      parse(content, isPlayerOneTurn)
    }
  }
}

case class GameSummary(
    winner: Int,
    rounds: Int,
    timeTakenNanos: Long,
    nodesP1: Long,
    nodesP2: Long,
    depth: Int,
    boardSize: Int,
    heuristicP1: String,
    heuristicP2: String,
    finalState: BreakthroughState
)

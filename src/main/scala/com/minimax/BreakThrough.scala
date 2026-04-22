package com.minimax

class BreakThrough(
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

  def play() =
    println("Rozpoczynamy grę w breakthrough!")
    println(display(game))

    while (!game.isGameOver) {
      if game.isPlayerOneTurn then {
        println("White to move")
        game = playerOne.move(game)
      } else {
        println("Black to move")
        game = playerTwo.move(game)
      }
      println(display(game))
    }
    if (game.playerOneWin) println("Grę wygrywa gracz pierwszy")
    else println("Grę wygrywa gracz drugi")

  def display(state: BreakthroughState): String = {
    (boardSize to 1 by -1)
      .map(row => {
        (1 to boardSize)
          .map(column => {
            state.pawnOwner(
              BreakthroughState.offset(row, column, boardSize)
            ) match {
              case PawnOwner.Empty     => EMPTY_CELL
              case PawnOwner.LastMove  => LAST_MOVE
              case PawnOwner.PlayerOne => PLAYER_ONE
              case PawnOwner.PlayerTwo => PLAYER_TWO
            }

          })
          .mkString(" ")
      })
      .mkString("\n")

  }

  final val PLAYER_ONE = "W"
  final val PLAYER_TWO = "B"
  final val EMPTY_CELL = "_"
  final val LAST_MOVE = "o"

}

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
    while (!game.isGameOver) {
      if game.isPlayerOneTurn then {
        println("Ruch gracza pierwszego")
        game = playerOne.move(game)
      } else {
        println("Ruch gracza drugiego")
        game = playerTwo.move(game)
      }
      println(display(game))
    }
    if (game.playerOneWin) println("Grę wygrywa gracz pierwszy")
    else println("Grę wygrywa gracz drugi")

  def display(state: BreakthroughState): String = {
    (1 to boardSize)
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

  final val PLAYER_ONE = "B"
  final val PLAYER_TWO = "W"
  final val EMPTY_CELL = "_"
  final val LAST_MOVE = "o"

}

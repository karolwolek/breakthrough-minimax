package com.minimax

@main def game() = {
  val game = new BreakThrough(
    new HumanPlayer(),
    new HumanPlayer(),
    8
  )

  game.play()
}

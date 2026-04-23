package com.minimax

@main def game() = {
  val game = new BreakThrough(
    new MiniMaxPlayer(3, ProgressiveHeurestic),
    new MiniMaxPlayer(3, ProgressiveHeurestic),
    8
  )

  game.play()
}

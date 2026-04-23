package com.minimax

@main def game() = {
  val game = new Breakthrough(
    new MiniMaxPlayer(3, ProgressiveHeurestic),
    new MiniMaxPlayer(3, ProgressiveHeurestic),
    8
  )

  game.play()
}

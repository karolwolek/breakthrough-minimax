package com.minimax

trait Player[S <: State[S]] {

  // this methods is for any player (human, computer or another process)
  // to produce the state from given state
  def move(s: S): S

  def nodesVisited(): Int
}

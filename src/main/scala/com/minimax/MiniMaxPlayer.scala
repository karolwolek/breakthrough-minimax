package com.minimax

import scala.io.StdIn

class MiniMaxPlayer[S <: State[S]](miniMaxDepth: Int, heuristic: Heuristic[S])
    extends Player[S] {

  var visited: Int = 0
  override def nodesVisited() = visited

  override def move(s: S): S = s.generateStates
    .map { state =>
      val score = -1 * negamax(state, miniMaxDepth - 1)
      new Evaluation(state, score)
    }
    .maxBy(_.score)
    .state

  def negamax(s: S, depth: Int): Double =
    visited += 1
    if depth == 0 || s.isGameOver then heuristic(s)
    else
      s.generateStates.foldLeft(Double.NegativeInfinity)((max, state) => {
        val score = -1 * negamax(state, depth - 1)
        if score > max then score
        else max
      })

}

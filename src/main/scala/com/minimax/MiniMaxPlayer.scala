package com.minimax

import scala.io.StdIn

case class Evaluation[S <: State[S]](state: S, score: Double)

class MiniMaxPlayer[S <: State[S]](miniMaxDepth: Int, heurestic: Heurestic[S])
    extends Player[S] {

  override def move(s: S): S = s.generateStates
    .map { state =>
      val score = -1 * negamax(state, miniMaxDepth - 1)
      new Evaluation(state, score)
    }
    .maxBy(_.score)
    .state

  def negamax(s: S, depth: Int): Double =
    if depth == 0 || s.isGameOver then heurestic(s)
    else
      s.generateStates.foldLeft(Double.NegativeInfinity)((max, state) => {
        val score = -1 * negamax(state, depth - 1)
        if score > max then score
        else max
      })

}

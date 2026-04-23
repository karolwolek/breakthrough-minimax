package com.minimax

trait Heuristic[S <: State[S]] {
  def apply(s: S): Double
}

package com.minimax

trait Heurestic[S <: State[S]] {
  def apply(s: S): Double
}

/*
 * Copyright 2026 Karol Wołkowski karolwolek06@gmail.com
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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

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

object PawnsAdvantageHeuristic extends Heuristic[BreakthroughState] {

  override def apply(s: BreakthroughState): Double =
    if (s.isPlayerOneTurn) {
      if (s.playerOneWin) return 100000.0
      if (s.playerTwoWin) return -100000.0

      (s.whitePositions.size - s.blackPositions.size).toDouble
    } else {
      if (s.playerTwoWin) return 100000.0
      if (s.playerOneWin) return -100000.0

      (s.blackPositions.size - s.whitePositions.size).toDouble
    }
}

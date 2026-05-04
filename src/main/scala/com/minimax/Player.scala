/*
 * Copyright 2017 Yurii Lahodiuk (yura.lagodiuk@gmail.com)
 * Modified by 2026 Karol Wołkowski (karolwolek06@gmail.com)
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

case class Evaluation[S <: State[S]](state: S, score: Double)

trait Player[S <: State[S]] {

  // this methods is for any player (human, computer or another process)
  // to produce the state from given state
  def move(s: S): S

  def nodesVisited(): Int
}

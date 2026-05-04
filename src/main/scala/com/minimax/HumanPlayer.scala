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

class HumanPlayer extends Player[BreakthroughState] {

  override def move(s: BreakthroughState): BreakthroughState =
    s.makeMove(chooseMove(movesForPawn(s))) match {
      case Some(state) => state
      case None        => {
        println("Wrong move mi amigo")
        move(s)
      }
    }

  def movesForPawn(s: BreakthroughState): List[Move] =
    println("Input the row and the column separated by space to choose a pawn")
    val (row, col) = StdIn.readf2("{0, number} {1,number}")
    val pos: Int = BreakthroughState.offset(
      row.asInstanceOf[Long].toInt,
      col.asInstanceOf[Long].toInt,
      s.boardSize
    )
    s.movesForPawn(pos) match {
      case Some(value) =>
        value match {
          case head :: tail => value
          case List()       => {
            println("No options to move")
            movesForPawn(s)
          }
        }
      case None => {
        println("Wrong choice")
        movesForPawn(s)
      }
    }

  def chooseMove(moves: List[Move]): Move =
    println("Choose a move")

    val options = moves
      .zip(1 to 3)
      .tapEach(option => {
        println(s"#${option._2}: ${option._1.direction}")
      })

    val choice = StdIn.readf1("{0, number}").asInstanceOf[Long].toInt
    if (choice > options.size || choice < 1) then chooseMove(moves)
    else
      options.find((move, index) => index == choice) match {
        case Some((move, index)) => move
        case None                => chooseMove(moves)
      }

  override def nodesVisited() = 0
}

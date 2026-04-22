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
    val pos: Int =
      (row.asInstanceOf[Long].toInt * s.boardSize) % s.boardSize + col
        .asInstanceOf[Long]
        .toInt
    s.movesForPawn(pos) match {
      case Some(value) => value
      case None        => movesForPawn(s)
    }

  def chooseMove(moves: List[Move]): Move =
    println("Choose a move")

    var i = 1
    moves.foreach(move => {
      println(s"$i: ${move.direction}")
      i += 1
    })

    val options = moves
      .zip(1 to 3)
      .tapEach(option => {
        println(s"$option._2: ${option._1.direction}")
      })

    val choice = StdIn.readf1("{0, number}").asInstanceOf[Long].toInt
    if (choice > options.size || choice < 1) then chooseMove(moves)
    else
      options.find((move, index) => index == choice) match {
        case Some((move, index)) => move
        case None                => chooseMove(moves)
      }
}

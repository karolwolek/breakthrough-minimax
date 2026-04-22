package com.minimax

import scala.io.StdIn

class HumanPlayer extends Player[BreakthroughState] {

  override def move(s: BreakthroughState): BreakthroughState =
    s.makeMove(chooseMove(moves(s))) match {
      case Some(state) => state
      case None        => {
        println("Wrong move mi amigo")
        move(s)
      }
    }

  def moves(s: BreakthroughState): List[(Int, Int)] =
    println("Input the row and the column separated by space to choose a pawn")
    val (row, col) = StdIn.readf2("{0, number} {1,number}")
    val pos: Int =
      (row.asInstanceOf[Long].toInt * s.boardSize) % s.boardSize + col
        .asInstanceOf[Long]
        .toInt
    s.movesForPawn(pos) match {
      case Some(value) => value.zip(1 to 3)
      case None        => moves(s)
    }

  // returns the pawn positon
  def chooseMove(moves: List[(Int, Int)]): Int =
    println("Choose a move")

    // max 3 moves
    moves.foreach(println(_))

    val choice = StdIn.readf1("{0, number}").asInstanceOf[Long].toInt
    if (choice > moves.size || choice < 1) then chooseMove(moves)
    else
      moves.find((index, pos) => index == choice) match {
        case Some(value) => value._2
        case None        => chooseMove(moves)
      }
}

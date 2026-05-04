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

import java.io.File
import scopt.OParser
import java.io.{PrintWriter, File}

val heuristicTypes = Set("alfabeta", "minimax")
val allowedTypes = Set("human") union heuristicTypes
val allowedHeuristics = Set("progressive", "balanced", "simple")

case class Config(
    p1Type: String = "",
    p1Heuristic: Option[String] = None,
    p2Type: String = "",
    p2Heuristic: Option[String] = None,
    depth: Int = 3,
    inputFile: Option[File] = None,
    boardSize: Int = 8,
    mode: String = "default",

    tournamentOutput: File = File("tournament_results.csv"),
    depthStart: Int = 3,
    depthEnd: Int = 5
)

@main def game(args: String*) = {
  val builder = OParser.builder[Config]
  val parser1 = {
    import builder._
    OParser.sequence(
      programName("minimax"),
      head("breakthrough minimax algorithm implementation", "0.1"),

      opt[String]("p1")
        .action((x, c) => c.copy(p1Type = x.toLowerCase))
        .validate(x =>
          if (allowedTypes.contains(x.toLowerCase)) success
          else failure(s"p1 must be ${allowedTypes.mkString("or")}")
        ),
      opt[String]("h1")
        .action((x, c) => c.copy(p1Heuristic = Some(x.toLowerCase)))
        .validate(x =>
          if (allowedHeuristics.contains(x.toLowerCase)) success
          else
            failure(
              s"Invalid heuristic. Choose: ${allowedHeuristics.mkString(", ")}"
            )
        ),

      opt[String]("p2")
        .action((x, c) => c.copy(p2Type = x.toLowerCase))
        .validate(x =>
          if (allowedTypes.contains(x.toLowerCase)) success
          else failure(s"p1 must be ${allowedTypes.mkString("or")}")
        ),
      opt[String]("h2")
        .action((x, c) => c.copy(p2Heuristic = Some(x.toLowerCase)))
        .validate(x =>
          if (allowedHeuristics.contains(x.toLowerCase)) success
          else
            failure(
              s"Invalid heuristic. Choose: ${allowedHeuristics.mkString(", ")}"
            )
        ),

      opt[Int]('d', "depth")
        .action((x, c) => c.copy(depth = x)),

      opt[Int]('s', "size")
        .action((x, c) => c.copy(boardSize = x)),

      opt[File]('i', "input")
        .action((x, c) => c.copy(inputFile = Some(x)))
        .text("Optional input file for initial board state"),

      cmd("tournament")
        .action((_, c) => c.copy(mode = "tournament"))
        .text("Make a tournament between heuristics and save results to csv")
        .children(
          opt[File]('o', "output")
            .action((x, c) => c.copy(tournamentOutput = x))
            .text("Where to save the tournament CSV results"),

          opt[Int]("start")
            .action((x, c) => c.copy(depthStart = x))
            .text("Starting depth for tournament"),

          opt[Int]("end")
            .action((x, c) => c.copy(depthEnd = x))
            .text("Ending depth for tournament")
        ),

      checkConfig(c =>
        if (c.mode == "tournament") success
        else {
          if (c.p1Type.isEmpty || c.p2Type.isEmpty)
            failure("Single game mode requires --p1 and --p2")
          else if (heuristicTypes.contains(c.p1Type) && c.p1Heuristic.isEmpty)
            failure("Player 1 is AI and requires a heuristic (--h1)")
          else if (heuristicTypes.contains(c.p2Type) && c.p2Heuristic.isEmpty)
            failure("Player 2 is AI and requires a heuristic (--h2)")
          else success
        }
      )
    )
  }
  OParser.parse(parser1, args, Config()) match {
    case Some(config) =>

      def getHeuristic(name: String): Heuristic[BreakthroughState] =
        name.toLowerCase match {
          case "progressive" => ProgressiveHeuristic
          case "balanced"    => BalancedLeadHeuristic
          case "simple"      => PawnsAdvantageHeuristic
          case _             =>
            throw new IllegalArgumentException(s"Unknown heuristic: $name")
        }

      def createPlayer(
          pType: String,
          heuristicName: Option[String]
      ): Player[BreakthroughState] = {
        pType match {
          case "human"    => new HumanPlayer()
          case "alfabeta" =>
            new AlfaBetaPlayer(config.depth, getHeuristic(heuristicName.get))
          case "minimax" =>
            new MiniMaxPlayer(config.depth, getHeuristic(heuristicName.get))
        }
      }

      def runTournament(heuristicNames: List[String], depths: Range) = {
        val results: Seq[GameSummary] = for {
          h1Name <- heuristicNames
          h2Name <- heuristicNames
          d <- depths
        } yield {
          val p1 = new AlfaBetaPlayer(d, getHeuristic(h1Name))
          val p2 = new AlfaBetaPlayer(d, getHeuristic(h2Name))
          val game =
            new Breakthrough(p1, p2, 8)

          println(s"Running: $h1Name vs $h2Name at depth $d...")
          game.playWithResult(d, h1Name, h2Name)
        }
        saveToCsv(results, config.tournamentOutput)
      }

      def saveToCsv(results: Seq[GameSummary], file: File): Unit = {
        val writer = new PrintWriter(file)
        try {
          // CSV Header
          writer.println(
            "p1_heuristic,p2_heuristic,depth,winner,rounds,time_seconds,nodes_p1,nodes_p2"
          )

          results.foreach { r =>
            val timeSec = r.timeTakenNanos / 1_000_000_000.0
            writer.println(
              s"${r.heuristicP1},${r.heuristicP2},${r.depth},${r.winner},${r.rounds},$timeSec,${r.nodesP1},${r.nodesP2}"
            )
          }
          println(s"\nResults successfully saved to ${file.toString()}")
        } finally {
          writer.close()
        }
      }

      config.mode match {
        case "tournament" => runTournament(allowedHeuristics.toList, 3 to 5)
        case "default"    => {
          val p1 = createPlayer(config.p1Type, config.p1Heuristic)
          val p2 = createPlayer(config.p2Type, config.p2Heuristic)

          val game =
            new Breakthrough(p1, p2, config.boardSize)
          game.playVerbose()

        }
      }

    case _ => // Error messages handled by scopt
  }
}

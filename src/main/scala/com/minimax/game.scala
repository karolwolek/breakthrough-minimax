package com.minimax

import java.io.File
import scopt.OParser

val allowedTypes = Set("human", "ai")
val allowedHeuristics = Set("progressive", "balanced", "simple")

case class Config(
    p1Type: String = "",
    p1Heuristic: Option[String] = None,
    p2Type: String = "",
    p2Heuristic: Option[String] = None,
    depth: Int = 3,
    inputFile: Option[File] = None,
    boardSize: Int = 8
)

@main def game(args: String*) = {
  val builder = OParser.builder[Config]
  val parser1 = {
    import builder._
    OParser.sequence(
      programName("minimax"),
      head("breakthrough minimax algorithm implementation", "0.1"),

      // Player 1 Setup
      opt[String]("p1")
        .required()
        .action((x, c) => c.copy(p1Type = x.toLowerCase))
        .validate(x =>
          if (allowedTypes.contains(x.toLowerCase)) success
          else failure("p1 must be 'human' or 'ai'")
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

      // Player 2 Setup
      opt[String]("p2")
        .required()
        .action((x, c) => c.copy(p2Type = x.toLowerCase))
        .validate(x =>
          if (allowedTypes.contains(x.toLowerCase)) success
          else failure("p2 must be 'human' or 'ai'")
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

      // CROSS-FIELD VALIDATION
      checkConfig(c =>
        if (c.p1Type == "ai" && c.p1Heuristic.isEmpty)
          failure("Player 1 is AI and requires a heuristic (--h1)")
        else if (c.p2Type == "ai" && c.p2Heuristic.isEmpty)
          failure("Player 2 is AI and requires a heuristic (--h2)")
        else success
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
          case "human" => new HumanPlayer()
          case "ai"    =>
            // We know heuristicName is present because of checkConfig validation
            new MiniMaxPlayer(config.depth, getHeuristic(heuristicName.get))
        }
      }

      val p1 = createPlayer(config.p1Type, config.p1Heuristic)
      val p2 = createPlayer(config.p2Type, config.p2Heuristic)

      val game =
        new Breakthrough(p1, p2, config.boardSize)
      game.playVerbose()

    case _ => // Error messages handled by scopt
  }
}

# University project about minimax ai algorithm implemented in scala for breakthrough board game

## Breakthrough

This projects is a try of an implementation of minimax algorithm in Breakthrough
board game. It is heavily inspired by the project of the user
[lagodiuk](https://github.com/lagodiuk/tic-tac-toe-minimax-scala) on github. The
logic is changed for the proper breakthrough game, and the minimax
implementation is different but the project structure is very similar

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it
with `sbt run`, and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).

You can use the program inside sbt console. Type `--help` to get the allowed options:

```
Usage: minimax [tournament] [options]

  --p1 <value>
  --h1 <value>
  --p2 <value>
  --h2 <value>
  -d, --depth <value>
  -s, --size <value>
  -i, --input <value>      Optional input file for initial board state
Command: tournament [options]
Make a tournament between heuristics and save results to csv
  -o, --output <value>     Where to save the tournament CSV results
  --start <value>          Starting depth for tournament
  --end <value>            Ending depth for tournament
```

**Choose player**

- You need to provide 2 types of players that will be instantianed at the beginning of the program and used by the program itself. The inputs are `p1` and `p2`
- The allowed options are:
  - `alfabeta` - computer playing with alfabeta pruning algorithm
  - `minimax`- computer playing with basic minimax without alfabeta pruning.
  - `human`

**Choose heuristic** (skip for human)

- If computer is used as a player, its corresponing `h1` or `h2` heuristic must be specified. The allowed options are:
  - progressive
  - balanced
  - simple

> [!TIP]
>
> It's also possible to make a tornament between all the heurestics, with given ai algorithm tree search depth. Example results are in repository

### heuristics explained

#### simple

The simpliest heuristic. It focuses mainly on the number of pawns on the board, ignoring their position.

$score = N_{player} - N_{oponnent}$

#### progressive

Prioritizes pawns that are closest to the enemy starting line exponentially.

$score = \sum_{p \in positions}{2^{progress(p)}}$

### balanced

It's a compromise between managing the high number of pawns, and promoting the leaders (linarly)

$score = \sum_{p \in positions}{(10 + 2 \cdot progress(p))} + 5 \cdot max(scores_{pawn})$

- where $scores$ is a set of individual scores, before summing.

> [!NOTE]
>
> Behaves the best, based on quick tests.

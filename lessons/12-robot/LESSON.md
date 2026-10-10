# Lesson 12 (challenge): the smart robot

**Files to edit:** `SmartRobot.java` (in `lessons/12-robot/`)

Everything so far comes together here: classes, an interface, lists, maps or
sets, loops, and a real algorithm. This one is hard. It is supposed to be.
Expect it to take more than one sitting.

## The field

An FTC field is 12 feet by 12 feet. Picture it as a grid of 12 x 12 = 144
tiles. Some tiles are blocked by walls. Your robot starts on one tile (S) and
has to drive to a target tile (T). See one now:

    mise run field 2

The robot moves one tile at a time: UP, DOWN, LEFT or RIGHT. No diagonals.
It cannot enter a wall tile or leave the field.

Rows are numbered from the top (row 0) to the bottom (row 11). Columns go
from the left (col 0) to the right (col 11). So UP makes the row SMALLER.

## What you are given

Read these files before writing anything. Reading code that other people
wrote is half of programming.

- `Direction.java`: an **enum**, a type with a fixed set of values: `UP`,
  `DOWN`, `LEFT`, `RIGHT`. `Direction.values()` gives you all four.
- `Position.java`: a row and a column. `position.step(Direction.UP)` gives
  the Position one tile up. It is a **record**, which means Java writes
  `equals` and `hashCode` for it, so Positions work properly as HashMap keys
  and inside a HashSet.
- `Field.java`: the grid. `field.getStart()`, `field.getTarget()`,
  `field.isOpen(position)` (false for walls and for anything off the field).
- `Robot.java`: the interface. Just one method:

      List<Direction> findPath(Field field);

- `BeelineRobot.java`: a finished robot that implements `Robot`. It is not
  smart: it drives straight at the target and ignores walls. Read it to see
  how a robot uses Field, Position and Direction.

`Main.java` runs a robot on a field and draws its path. Try it now with the
BeelineRobot and watch it crash:

    mise run try 12

## Your job

Open `SmartRobot.java`. It already `implements Robot`, but its `findPath`
just gives up and returns `null`. Make it smart.

`findPath` must return the moves for a SHORTEST path from the start to the
target: the fewest steps possible, never touching a wall.
If the target cannot be reached at all, return `null`.

## How can a robot find the shortest path?

Here is an idea, with no code. Turning it into Java is your job.

Imagine pouring water on the start tile. After 1 second it has spread to the
tiles next to the start. After 2 seconds, to the tiles next to those. The
water spreads one tile per second, in every open direction at once, and it
flows around walls.

Write on each tile the second at which the water first reached it:

      S = start (0)       # = wall

      0  1  2  3  4
      1  #  #  #  5
      2  #  8  7  6
      3  #  7  #  7
      4  5  6  7  8

The number on a tile is exactly the fewest steps needed to get there. (Why?
Could the water have arrived any sooner?) So the number on the target tile
is the length of the shortest path.

To do this in a program, you need:

1. A way to remember each tile's number, or at least which tiles are
   already wet. (You have a class that works as a HashMap key...)
2. A **to-do list** of wet tiles whose neighbors you have not looked at yet.
   Start with just the start tile on it. Take the OLDEST tile off the list,
   look at its four neighbors, and for every neighbor that is open and still
   dry: record it, and add it to the END of the to-do list. Repeat until the
   list is empty or you reach the target.

   Taking the oldest first is what makes the water spread evenly. A to-do
   list where you add at the back and take from the front is called a
   **queue**. Java has one ready-made: `ArrayDeque`.

That gets you the DISTANCE. You need the actual MOVES. Two ideas, pick one:

- When water reaches a tile, also remember which tile it came from (or which
  Direction it was moving). Then start at the target and follow those
  memories backward to the start. You will collect the moves in reverse.
- Or: start at the target and repeatedly step to any neighbor whose number
  is one less.

This recipe has a name, **breadth-first search**. It is how map apps and
game characters find routes. Look up the name once you have made your own
attempt.

## Work in small steps

1. Run `mise run check 12` before you change anything. The "Boxed in"
   field already passes, because giving up is the right answer there!
2. Make it spread the water and just print the number for the target tile.
   Compare with `mise run field 1`, which tells you the right answer.
3. Then work on turning that into the list of moves.
4. Use `Main.java` to draw your path. Change `new BeelineRobot()` to
   `new SmartRobot()`. `mise run field 3` draws your path on the checker's
   fields.

There are 5 fields: an open one, a wall, a trap, a maze, and one where the
target is boxed in.

## Look it up

- ArrayDeque (a queue: see `add`, `poll`, `isEmpty`):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/ArrayDeque.html
- HashMap:
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/HashMap.html
- HashSet:
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/HashSet.html
- Collections (see `reverse`):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Collections.html

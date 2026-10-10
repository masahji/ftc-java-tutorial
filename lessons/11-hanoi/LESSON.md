# Lesson 11: Towers of Hanoi

A famous puzzle, and the best showcase for recursion there is.

There are three pegs. A stack of disks sits on the first peg, biggest on the
bottom, smallest on top:

        |             |             |
       [1]            |             |
      [ 2 ]           |             |
     [  3  ]          |             |
    ---------     ---------     ---------
        A             B             C

Move the whole stack to another peg. The rules:

1. Move one disk at a time.
2. You may only take the TOP disk of a peg.
3. Never put a bigger disk on top of a smaller one.

Try it by hand first, with 3 coins of different sizes. Three disks can be
moved in 7 moves. Really do this. It makes the rest much easier.

## Thinking recursively

With a loop, this puzzle is miserable. With recursion, it is a handful of
lines. Don't think about individual moves. Ask the recursion question:

> Suppose a friend can already solve the puzzle for 2 disks, moving them
> from any peg to any other peg. How would you use your friend to solve it
> for 3 disks?

Look at the biggest disk. When is it allowed to move? Where does everything
else have to be at that moment? What happens after it moves?

Then: what is the base case? What number of disks is so easy that the
answer is obvious?

Notice that the pegs change jobs. The peg that is the "spare" for the big
problem is the destination for one of the smaller problems.

## Your job

Open `lessons/11-hanoi/Hanoi.java` and write:

    static List<String> solve(int disks, String from, String to, String spare)

It returns the list of moves that takes `disks` disks from the peg named
`from` to the peg named `to`, using `spare` as the extra peg.

Each move is a String: the peg you take the top disk from, then `->`, then
the peg you put it on. For example `"A->C"`.

    solve(1, "A", "C", "B")   ->   ["A->C"]
    solve(2, "A", "C", "B")   ->   ["A->B", "A->C", "B->C"]

Use the peg names you are given. The checker also asks for
`solve(3, "left", "middle", "right")`.

No loops allowed. You may add a second helper method if you like (for
example, one that takes the list of moves as an extra parameter and adds to
it), or you can build the list from the smaller lists with `addAll`.

The checker plays your moves on a real set of pegs. It tells you the first
move that breaks a rule, and whether you used the fewest possible moves.
`Main.java` prints your moves so you can follow them with your coins.

After you pass: 10 disks take 1023 moves. Can you work out the formula for
n disks? The legend says monks are moving 64 golden disks, one per second,
and the world ends when they finish. How long is that?

## Look it up

- List (see `add`, `addAll`):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/List.html

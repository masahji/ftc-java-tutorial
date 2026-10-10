# FTC Java Tutorial: Java Basics

Twelve hands-on lessons that take you from `Hello, World!` to programming a
robot that finds its own way through a maze.

You read a lesson, write the code yourself, and run a checker that tells you
what works and what doesn't yet. **There are no answers in this repo**, and
that is on purpose. Working it out is how it sticks.

## Setup (once)

1. Install [mise](https://mise.jdx.dev/getting-started.html):
   ```sh
   curl https://mise.run | sh
   ```
2. Get the code and install Java:
   ```sh
   git clone https://github.com/masahji/ftc-java-tutorial.git
   cd ftc-java-tutorial
   mise trust
   mise install
   ```
3. Make sure it works:
   ```sh
   mise run lessons
   ```

## How a lesson works

Every lesson is the same four steps. Here they are for lesson 1:

| Step | Command | What happens |
| --- | --- | --- |
| 1. Read | `mise run lesson 1` | Prints the lesson: the idea, an example, and your job |
| 2. Write | (your editor) | Edit the files in the lesson's folder under `lessons/` |
| 3. Try | `mise run try 1` | Runs your code so you can see what it does |
| 4. Check | `mise run check 1` | Tests your code and prints `[PASS]` or `[FAIL]` for each test |

Repeat steps 2 to 4 until every check passes. Then move on to the next
lesson.

Good to know:

- Leave the number off (`mise run lesson`, `mise run check`) and you get your
  next unfinished lesson.
- `mise run lessons` shows the whole list and which ones you have finished.
- You can also read a lesson in your editor or on GitHub. It is the
  `LESSON.md` file in the lesson's folder.

## The lessons

| # | Lesson | Folder |
| --- | --- | --- |
| 1 | Hello, World and static methods | `lessons/01-hello` |
| 2 | Warm-up: methods, ifs and loops | `lessons/02-warmup` |
| 3 | Classes and objects | `lessons/03-classes` |
| 4 | Inheritance | `lessons/04-07-shapes` |
| 5 | Abstract classes and polymorphism | `lessons/04-07-shapes` |
| 6 | Interfaces | `lessons/04-07-shapes` |
| 7 | Color and method chaining | `lessons/04-07-shapes` |
| 8 | Lists and simple algorithms | `lessons/08-lists` |
| 9 | HashMap: counting things | `lessons/09-hashmaps` |
| 10 | Recursion | `lessons/10-recursion` |
| 11 | Towers of Hanoi | `lessons/11-hanoi` |
| 12 | Challenge: the smart robot | `lessons/12-robot` |

Do them in order. Lessons 4 to 7 share one folder because you keep building
on the same shape classes.

## Reading the checker

```
  [PASS] square(0) returns 0
  [FAIL] square(4) returns 16
         square(4)
           expected: 16
           but got:  0
```

- Fix the **first** `[FAIL]` first. Later ones often fix themselves.
- "expected" is what the method should have returned. "but got" is what
  yours returned.
- If your code crashes, the checker shows the error and the line number in
  your file.
- If Java cannot compile your code, nothing runs. Read the first error: it
  names the file and line, and the `^` points at the spot.
- Anything you `System.out.println` while a failing test runs is shown under
  that test. Printing values is a great way to find out what your code is
  really doing.

## When you are stuck

1. Read the `[FAIL]` message again, slowly. Then re-read the lesson.
2. Use `Main.java` in the lesson folder to experiment (`mise run try 3`).
   Print things.
3. Try one line at a time in `mise run shell` (jshell).
4. Look it up in the Javadoc (next section).
5. Explain the problem out loud to a teammate, line by line.
6. Ask a mentor. Show what you tried.

Looking up how a Java method works is not cheating. It is what programmers do
all day. Pasting in a whole solution from the internet or an AI *is*
cheating, mostly yourself: the checks will pass and you will have learned
nothing you can use on the robot.

## The Javadoc

The Javadoc is the official manual for everything built into Java. Every
lesson ends with a "Look it up" section linking to the pages you need.

- Everything: https://docs.oracle.com/en/java/javase/25/docs/api/index.html
- [String](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html)
- [Math](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Math.html)
- [List](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/List.html)
  and [ArrayList](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/ArrayList.html)
- [Map](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Map.html)
  and [HashMap](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/HashMap.html)
- [Random](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Random.html)
- [Comparable](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Comparable.html)
- The Java Tutorials (longer explanations): https://docs.oracle.com/javase/tutorial/java/

How to read a Javadoc page: skip to **Method Summary**. Each row is one
method. The left column is the type it returns, then its name and
parameters, then one sentence about what it does. Click a name for details.
The search box at the top right finds any class or method.

## All the commands

| Command | What it does |
| --- | --- |
| `mise run lessons` | List the lessons and your progress |
| `mise run lesson 3` | Read lesson 3 |
| `mise run try 3` | Run your code for lesson 3 |
| `mise run check 3` | Check your work on lesson 3 |
| `mise run field 2` | Lesson 12: draw challenge field 2 and your robot's path |
| `mise run example Variables` | Run a finished example from `src/` |
| `mise run shell` | Open jshell to try Java one line at a time |
| `mise run clean` | Delete compiled files (your code and progress stay) |

## What is in this repo

- `lessons/`: the lessons and your code. This is where you work.
- `src/`: short finished examples of the basics (variables, loops, lists,
  maps, a class). Reference material, not answers.
- `tools/`: the program behind `mise run lesson` and `mise run check`. You
  are welcome to read it. It says what gets tested, not how to solve it.
- `.progress`: where your finished lessons are remembered (only on your
  computer).

## For mentors

- The checks load student classes by name, so a missing class or method
  produces a readable message instead of a wall of compiler errors.
- No solutions are stored here. Lessons 11 and 12 are graded by replaying
  the student's moves, so the checker contains no solver either.
- To add a lesson: add a folder under `lessons/` with a `LESSON.md` and
  starter files, write a `tools/CheckNN....java`, and register it in the
  `LESSONS` list in `tools/Tutor.java`.

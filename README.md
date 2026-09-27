# FTC Java Tutorial — Java Basics

A small starter project for learning core Java before writing FTC robot code.
Each file in `src/` is a self-contained lesson with its own `main` method.

## Setup

1. Install [mise](https://mise.jdx.dev/getting-started.html):
   ```sh
   curl https://mise.run | sh
   ```
2. From this directory, trust the config and install Java (Temurin 25):
   ```sh
   mise trust
   mise install
   java -version   # should print 25.x
   ```

## Commands

| Command                    | What it does                                        |
| -------------------------- | --------------------------------------------------- |
| `mise run lessons`         | List the lessons in `src/`                          |
| `mise run lesson Hello`    | Compile everything in `src/`, then run `Hello`      |
| `mise run quick Hello`     | Run `src/Hello.java` directly, no separate compile  |
| `mise run compile`         | Compile `src/*.java` into `out/`                    |
| `mise run shell`           | Open `jshell` to try Java one line at a time        |
| `mise run clean`           | Delete `out/`                                       |
| `mise tasks`               | Show all tasks                                      |

## What's happening under the hood

Java source (`.java`) is compiled to bytecode (`.class`), which the JVM runs:

```sh
javac -d out src/*.java   # compile
java -cp out Hello        # run the Hello class from out/
java src/Hello.java       # shortcut: compile in memory and run one file
```

## Lessons

Work through them in order:

1. `Hello` — the smallest Java program
2. `Variables` — primitive types, `String`, `var`
3. `ControlFlow` — `if`/`else`, `for`, `while`, `switch`
4. `Methods` — parameters and return values
5. `ListsAndMaps` — arrays, `List`, `Map`
6. `Classes` — fields, constructors, methods, `record`

## Adding a lesson

Create `src/MyLesson.java` with a matching class name:

```java
public class MyLesson {
    public static void main(String[] args) {
        System.out.println("Hello from MyLesson");
    }
}
```

Then run `mise run lesson MyLesson`.

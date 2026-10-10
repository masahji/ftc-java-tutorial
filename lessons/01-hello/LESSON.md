# Lesson 1: Hello, World and static methods

**Files to edit:** `Hello.java` (in `lessons/01-hello/`)

Every Java program starts in a method called `main`. Java runs the lines
inside it from top to bottom.

```java
public class Greeter {
    public static void main(String[] args) {
        System.out.println("Good morning");
    }
}
```

## Static methods

A **static method** is a named chunk of code that belongs to the class. It can
take inputs (**parameters**) and hand back one output (the **return value**).

```java
static int doubleIt(int number) {
    return number * 2;
}
```

- `static`  : you can call it without creating an object first
- `int`     : the type of value it returns
- `doubleIt`: its name
- `(int number)`: it needs one int, which it will call `number`

You call it by name, and you can use what comes back:

```java
int result = doubleIt(21);          // result is 42
System.out.println(doubleIt(5));    // prints 10
```

A method can call other methods too. That is how big programs get built out
of small pieces:

```java
static int quadrupleIt(int number) {
    return doubleIt(doubleIt(number));
}
```

**Printing is not returning.** `System.out.println` shows text on the screen.
`return` hands a value back to whoever called the method. The checker looks at
what your methods *return*.

## Your job

Open `lessons/01-hello/Hello.java` and finish the TODOs:

1. Make `main` print exactly `Hello, World!`
2. Write a static method `greet` that takes a `String name` and returns
   `"Hello, " + name + "!"`. So `greet("Ada")` returns `Hello, Ada!`
3. Write a static method `shout` that takes a `String name` and returns the
   same greeting in ALL CAPITALS. So `shout("Ada")` returns `HELLO, ADA!`.
   Rule: `shout` must get its greeting by **calling `greet`**. Do not type
   "Hello" a second time.
4. Back in `main`, add two more lines: print `greet` with your own name, then
   print `shout` with your own name.

## Look it up

How do you turn a String into capitals? Strings come with dozens of built-in
methods. They are all listed in the Javadoc, the official manual for Java:

  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html

Scroll to "Method Summary" and find one whose name sounds right. Learning to
find things there is one of the most useful skills you can build.

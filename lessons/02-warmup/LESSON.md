# Lesson 2: Warm-up with methods, ifs and loops

**Files to edit:** `Warmup.java` (in `lessons/02-warmup/`)

Time to shake the rust off. Five small methods, each one a little harder.

## Reminders

Making a decision:

```java
if (speed > 100) {
    System.out.println("too fast");
} else if (speed > 50) {
    System.out.println("fast");
} else {
    System.out.println("slow");
}
```

Repeating something a known number of times:

```java
for (int i = 1; i <= 3; i++) {
    System.out.println("lap " + i);      // lap 1, lap 2, lap 3
}
```

Remainder: `%` gives what is left over after dividing. `17 % 5` is `2`.

Walking through a String one character at a time:

```java
String word = "java";
for (int i = 0; i < word.length(); i++) {
    char letter = word.charAt(i);        // 'j', then 'a', then 'v', then 'a'
}
```

A `char` uses single quotes (`'a'`), a `String` uses double quotes (`"a"`).
You compare chars with `==`.

## Your job

Open `lessons/02-warmup/Warmup.java`. Each method has a comment that says what
it should do, and a placeholder `return` so the file compiles. Replace the
placeholder with real code.

1. `square(n)`       : n times n
2. `isEven(n)`       : true when n is even
3. `largest(a, b, c)`: the biggest of three numbers
4. `sumTo(n)`        : 1 + 2 + 3 + ... + n
5. `countVowels(text)`: how many of a, e, i, o, u are in the text
                       (capital letters count too)

`Main.java` calls your methods and prints the results. Use it to experiment:

    mise run try 2

Change `Main.java` however you like. The checker ignores it.

## Look it up

- String (see `length`, `charAt`, `toLowerCase`):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html
- Math (see `max`, `min`, `abs`):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Math.html

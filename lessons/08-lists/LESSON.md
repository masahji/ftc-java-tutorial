# Lesson 8: Lists and simple algorithms

**Files to edit:** `ListAlgorithms.java` (in `lessons/08-lists/`)

An **algorithm** is a recipe: exact steps that turn an input into an answer.
This lesson is about the most common family of recipes: "walk through a
list and keep track of something as you go".

## List reminders

```java
import java.util.ArrayList;
import java.util.List;

List<Integer> scores = new ArrayList<>();
scores.add(70);
scores.add(95);
scores.add(82);

scores.size();      // 3
scores.get(0);      // 70   (positions start at 0)
scores.get(2);      // 82   (the last one is at size() - 1)

for (int score : scores) {                   // when you only need the values
    System.out.println(score);
}
for (int i = 0; i < scores.size(); i++) {    // when you need the position too
    System.out.println(i + ": " + scores.get(i));
}
```

A list holds `Integer` (capital I, an object), not `int`. Java converts
between the two for you almost everywhere.

## The pattern

Nearly every list algorithm has the same skeleton:

1. Make a variable to remember your answer so far.
2. Loop over the list.
3. At each element, maybe update the variable.
4. After the loop, the variable is the answer.

Here is the pattern finding the SHORTEST word in a list:

```java
static String shortest(List<String> words) {
    String best = words.get(0);              // 1. best so far
    for (String word : words) {              // 2. look at each one
        if (word.length() < best.length()) {
            best = word;                     // 3. found a better one
        }
    }
    return best;                             // 4. done
}
```

Why start with `words.get(0)` instead of some made-up value? Think about
what would go wrong.

## Your job

Open `lessons/08-lists/ListAlgorithms.java` and fill in each method.
Write the loops yourself. Do not use the list's own `indexOf`, `contains`,
`reversed`, or `Collections` helpers. The goal is to build the muscle.

1. `indexOf(numbers, target)`  : position of the first `target`, or -1
2. `countOf(numbers, target)`  : how many times `target` appears
3. `largest(numbers)`          : the biggest number (the list is not empty)
4. `average(numbers)`          : the mean, as a double (the list is not empty)
5. `reversed(numbers)`         : a NEW list in the opposite order; the list
                                 you were given must not change
6. `randomElement(numbers)`    : one element picked at random (not empty)

Traps: one of the tests for `largest` uses only negative numbers. One of the
tests for `average` has an answer that is not a whole number, and in Java
`3 / 2` is `1`.

For `randomElement` you need a random position. Java has a class for that,
`java.util.Random`. Read its Javadoc and find the method that gives you a
random int below a limit.

## Look it up

- List:
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/List.html
- ArrayList:
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/ArrayList.html
- Random:
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Random.html

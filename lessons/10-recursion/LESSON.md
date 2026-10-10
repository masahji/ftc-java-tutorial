# Lesson 10: Recursion

**Files to edit:** `Recursion.java` (in `lessons/10-recursion/`)

A **recursive** method is a method that calls itself.

That sounds like it should run forever. It doesn't, because each call works
on a SMALLER version of the problem, until the problem is so small that the
answer is obvious.

## An example

Count how many times the letter 'a' appears in a word, with no loop.

Think about it this way: the count for "banana" is

- 1 if the first letter is an 'a', otherwise 0
- PLUS the count for the rest of the word, "anana"

And the count for "anana" is the same idea again, with "nana". The word
keeps shrinking. When it is empty, the answer is obviously 0.

```java
static int countA(String word) {
    if (word.isEmpty()) {                    // BASE CASE: so small it's obvious
        return 0;
    }
    int first = word.charAt(0) == 'a' ? 1 : 0;
    return first + countA(word.substring(1)); // RECURSIVE CASE: a smaller problem
}
```

Every recursive method needs both parts:

1. **Base case**: the smallest input, answered directly, with no more calls.
2. **Recursive case**: call yourself on something smaller, then use that
   answer to build your own.

The trick to writing one is to TRUST the recursive call. Don't trace through
every level in your head. Ask: "If somebody handed me the answer for the
smaller problem, how would I finish the job?"

If you forget the base case, or the problem never gets smaller, the method
calls itself until Java runs out of room and stops with a
`StackOverflowError`. Everybody gets a few of those. It's fine.

## Your job

Open `lessons/10-recursion/Recursion.java`. The rule for this whole file:
NO LOOPS. The checker fails the lesson if it finds `for` or `while`.

1. `factorial(n)`: n! = n x (n-1) x ... x 2 x 1, and 0! is 1.
   Hint: 5! is 5 x 4!.
2. `sumDigits(n)`: the digits of n added up. `sumDigits(1234)` is 10.
   Hint: `1234 % 10` is the last digit, and `1234 / 10` is everything else.
3. `power(base, exponent)`: base multiplied by itself exponent times.
   Anything to the power 0 is 1.
4. `fib(n)`: the Fibonacci numbers 0, 1, 1, 2, 3, 5, 8, 13, ... Each one is
   the sum of the two before it. `fib(0)` is 0 and `fib(1)` is 1.
   This one calls itself TWICE.
5. `reverse(text)`: the text backward. `reverse("robot")` is "tobor".
6. `isPalindrome(text)`: true if the text reads the same both ways, like
   "racecar". Hint: compare the first and last letters. What is left?

For each one, write down two things before you type anything:
what is the base case? what is the smaller problem?

Notice that `factorial` and `power` return `long`, not `int`. An int tops out
around 2 billion, and 15! is much bigger than that.

## Look it up

- String (see `substring`, `charAt`, `length`, `isEmpty`):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html

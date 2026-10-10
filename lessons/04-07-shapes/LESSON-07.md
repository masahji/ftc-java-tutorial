# Lesson 7: Color and method chaining

**Files to edit:** `Color.java`, `Shape.java` (in `lessons/04-07-shapes/`)

Same folder: `lessons/04-07-shapes/`.

## Method chaining

When a method returns an object, you can call another method on the result
right away, without storing it in a variable first:

```java
String name = "  ada lovelace  ";
String tidy = name.trim().toUpperCase().replace(" ", "_");
// "ADA_LOVELACE"
```

Read it left to right: `trim()` returns a new String, `toUpperCase()` is
called on THAT String and returns another, and so on. Each link in the chain
runs on whatever the previous link returned.

The long way says exactly the same thing:

```java
String a = name.trim();
String b = a.toUpperCase();
String tidy = b.replace(" ", "_");
```

Two ways to design a class so it can be chained:

1. Return a **new object** each time. Strings work like this: `toUpperCase()`
   does not change the original, it gives you a new String.
2. Return **`this`** (the same object) at the end of a method that changes
   the object. `StringBuilder.append` works like this.

You will do one of each.

## Your job, part 1: Color

Screens make every color by mixing red, green and blue light. Each amount is
a whole number from 0 to 255. Web pages write colors as a "hex code":
`#` followed by two base-16 digits for each of red, green and blue.

    red 255, green 136, blue 0   ->   #FF8800
    red 5,   green 10,  blue 15  ->   #050A0F

Open `Color.java` and write:

1. constructor `Color(int red, int green, int blue)`
2. `int getRed()`, `int getGreen()`, `int getBlue()`
3. `String toHex()`: the hex code, like `"#FF8800"`. Capital letters, always
   exactly two digits per color (5 is `05`, not `5`). You do not have to do
   the base-16 math yourself: look up `String.format` and `Integer` below and
   find something that does it for you.
4. `Color withRed(int red)`, `Color withGreen(int green)`,
   `Color withBlue(int blue)`: each returns a NEW Color with one amount
   replaced. The original Color must not change.
5. `Color mix(Color other)`: returns a NEW Color where each of red, green and
   blue is the average of the two colors (use int division: 255 and 0
   average to 127).

Now this works:

```java
new Color(0, 0, 0).withRed(255).withGreen(136).toHex()    // "#FF8800"
```

## Your job, part 2: paint the shapes

Open `Shape.java` and add:

6. `Shape paint(Color color)`: remembers the color, then returns `this`.
7. `Color getColor()`: the color the shape was painted.

Because `paint` returns the shape itself, you can keep going:

```java
new Square(2).paint(orange).getColor().toHex()
new Rectangle(3, 4).paint(orange).describe()
```

Rectangle, Square and Triangle get `paint` for free. You only write it once,
in Shape.

## Look it up

- String.format (search the page for "format"):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html
- The format codes (`%d`, `%s`, `%x` ... look for "width" and the "0" flag):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Formatter.html
- Integer (see `toHexString`, and notice what it does with small numbers):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Integer.html

Tip: `mise run shell` opens jshell, where you can try one line at a time,
for example `String.format("%5d|", 42)`.

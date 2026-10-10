# Lesson 6: Interfaces

Same folder: `lessons/04-07-shapes/`.

## The problem

Your `Shapes.totalArea` adds up the area of any list of Shapes. Now think
about an FTC field tile, a solar panel, a sheet of plywood. They all have an
area. None of them is a "Shape" in your program, and it would be silly to
force them into that family. And a Java class can only `extends` ONE parent,
so a class that already has a parent could not join even if it wanted to.

We want to say: "I don't care what you ARE. I only care what you can DO."

## Interfaces

An **interface** is a list of methods with no bodies. It is a contract.

```java
public interface Speaker {
    String speak();
}
```

Any class can sign the contract with `implements`, as long as it really has
the methods:

```java
public class Robot implements Speaker {        // not an Animal at all
    public String speak() {
        return "BEEP";
    }
}

public abstract class Animal implements Speaker {   // so every Animal is one
    ...
}
```

And then code can be written against the contract:

```java
static void everybodySpeak(List<Speaker> speakers) {
    for (Speaker speaker : speakers) {
        System.out.println(speaker.speak());
    }
}
```

`everybodySpeak` works with dogs, robots, and classes that will not be
written until next year. It does not know or care what they are.

Abstract class or interface?

- Abstract class: "these things are the same KIND of thing and share code and
  fields." One parent only.
- Interface: "these things can all DO this." A class can implement as many
  interfaces as it likes: `class Robot implements Speaker, Movable, Chargeable`

FTC robot code is full of interfaces. A `DcMotor` is an interface: your code
works the same whichever brand of motor is plugged in.

## Your job, part 1: your own interface

1. Create `Measurable.java`: an interface with one method, `double area();`
2. Make `Shape` implement `Measurable`. (Shape already has an `area()`, so
   this is a one-line change.)
3. Create `Tile.java`: a square field tile. It must NOT extend Shape.
   - `implements Measurable`
   - constructor `Tile(double side)`
   - `area()` is side times side
4. Create `Measure.java` with
   `static double total(List<Measurable> things)`: all the areas added up.

The checker will hand `Measure.total` an object of a class that it invents on
the spot, one your code has never seen. If you wrote against the interface,
it just works.

## Your job, part 2: an interface Java already has

Java can sort a list for you with `Collections.sort(list)`. But how would it
know which of two Shapes comes first? By color? By name? It can't guess. So
Java publishes a contract called `Comparable`, and sorts anything that signs
it.

5. Make `Shape` implement `Comparable<Shape>` too:

       public abstract class Shape implements Measurable, Comparable<Shape>

   and write the one method the contract requires, in Shape:

       public int compareTo(Shape other)

   Shapes should be ordered by area, smallest first. Read the Javadoc for
   `Comparable` (below) to find out what number `compareTo` must return.
   Watch out: areas are doubles and `compareTo` returns an int. Two areas
   that differ by 0.5 are still different! `Double` has a method that may
   help.

Once that is done, `Collections.sort` works on your shapes. You wrote one
method, and Java's sorting code, written decades before your class existed,
can now use it. That is the power of an interface.

## Look it up

- Comparable (read the `compareTo` description carefully):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Comparable.html
- Double:
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Double.html
- Collections (see `sort`, `max`, `shuffle`):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Collections.html
- Interfaces in the official tutorial:
  https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html

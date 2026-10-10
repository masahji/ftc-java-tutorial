# Lesson 5: Abstract classes and polymorphism

**Files to edit:** `Shape.java`, `Triangle.java`, `Shapes.java` (in `lessons/04-07-shapes/`)

Same folder as lesson 4: `lessons/04-07-shapes/`.

## Part 1: abstract classes

Right now you can write `new Shape("blob")`, and its `area()` is 0. That is a
lie. A "shape in general" does not have an area. Only specific shapes do.

An **abstract class** is a parent that is not finished on purpose:

```java
public abstract class Animal {
    private String name;

    public Animal(String name) {
        this.name = name;
    }

    public abstract String sound();      // no body! every child MUST write it

    public String speak() {              // normal methods are still allowed
        return name + " says " + sound();
    }
}
```

- You cannot say `new Animal("x")` any more. Java refuses.
- Every non-abstract child must provide `sound()`, or it will not compile.
  The abstract method is a promise the children are forced to keep.

## Part 2: polymorphism

A variable of the parent type can point at any child object:

```java
Animal pet = new Dog("Rex");       // fine: a Dog is an Animal
pet = new Cat("Tom");              // also fine
```

So one list can hold many kinds of things, and one loop can handle them all:

```java
List<Animal> zoo = new ArrayList<>();
zoo.add(new Dog("Rex"));
zoo.add(new Cat("Tom"));
for (Animal animal : zoo) {
    System.out.println(animal.speak());   // Rex says Woof, Tom says Meow
}
```

The loop never asks "are you a Dog or a Cat?". It calls `speak()` and each
object runs its own version. That is **polymorphism** ("many shapes"). The
big win: add a `Cow` class next month and this loop works with no changes.

## Your job

1. Open `Shape.java`. Change `Shape` into an abstract class, and make `area()` and
   `perimeter()` abstract methods. `describe()` stays as it is.
   Run `mise run check 4` afterward: lesson 4 must still pass.
2. Open `Triangle.java`. Make `Triangle` extend `Shape`, then add:
   - constructor `Triangle(double a, double b, double c)`: its three sides
   - its name is `"Triangle"`
   - `perimeter()`: add up the sides
   - `area()`: use Heron's formula. With s = half the perimeter,

         area = square root of ( s * (s - a) * (s - b) * (s - c) )

     A 3-4-5 triangle has area 6. Find the square root method in the `Math`
     Javadoc below.
3. Open `Shapes.java` and finish its two static methods:
   - `static double totalArea(List<Shape> shapes)`: all the areas added up
     (0 for an empty list)
   - `static Shape largest(List<Shape> shapes)`: the shape with the biggest
     area (you may assume the list is not empty)

Your `Shapes` methods must not mention Rectangle, Square or Triangle at all.
They only know about `Shape`. That is the whole point.

## Look it up

- Math (square roots and more):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Math.html
- List (see `size`, `get`, `isEmpty`):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/List.html
- Abstract classes in the official tutorial:
  https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html

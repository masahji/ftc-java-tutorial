# Lesson 4: Inheritance

**Files to edit:** `Rectangle.java`, `Square.java` (in `lessons/04-07-shapes/`)

Lessons 4, 5, 6 and 7 all happen in this one folder, `lessons/04-07-shapes/`.
You will keep building on the same classes, the way real projects grow.

## The idea

Sometimes one class is a more specific version of another. Inheritance lets
the specific class (the **child**, or subclass) start with everything the
general class (the **parent**, or superclass) already has.

```java
public class Animal {
    private String name;

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String sound() {
        return "...";
    }

    public String speak() {
        return name + " says " + sound();
    }
}

public class Dog extends Animal {          // a Dog IS AN Animal
    public Dog(String name) {
        super(name);                       // run Animal's constructor first
    }

    @Override
    public String sound() {                // replace the parent's version
        return "Woof";
    }
}
```

```java
Dog rex = new Dog("Rex");
System.out.println(rex.getName());   // Rex            (inherited from Animal)
System.out.println(rex.speak());     // Rex says Woof  (inherited, but it
                                     //  calls Dog's sound!)
```

- `extends` : "start with everything this other class has"
- `super(...)` : calls the parent's constructor. It must be the first line of
  the child's constructor.
- `@Override` : "I am replacing a method my parent has." If you misspell the
  name, Java tells you instead of quietly making a brand new method.
- A child cannot touch its parent's `private` fields. It uses the parent's
  methods, like everybody else.

Inheritance can go more than one level: a `Puppy` could extend `Dog`, which
extends `Animal`.

## Your job

`Shape.java` is already written. Read it first. Do not change it yet.

1. Open `Rectangle.java`. Make `Rectangle` extend `Shape`, then add:
   - constructor `Rectangle(double width, double height)`
   - `double getWidth()` and `double getHeight()`
   - override `area()` and `perimeter()`
   - its name is `"Rectangle"`
2. Open `Square.java`. Make `Square` extend `Rectangle` (three levels!).
   - constructor `Square(double side)`
   - its name is `"Square"`
   - `Square` must NOT contain its own `area()` or `perimeter()`. A square is
     just a rectangle whose width and height are the same, so Rectangle's
     methods already give the right answer.

Something to puzzle over: Rectangle tells Shape its name is "Rectangle". So
how does a Square end up named "Square"? There is more than one good way.
(A class may have more than one constructor. A child may override a getter.)

You never have to write `describe()`. Rectangle and Square inherit it from
Shape. Look at how Shape's `describe()` calls `area()`, and think about
which `area()` actually runs.

Uncomment the lesson 4 lines in `Main.java` to try things:  `mise run try 4`

## Look it up

Inheritance in the official Java tutorial:
  https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html

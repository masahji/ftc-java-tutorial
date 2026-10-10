# Lesson 3: Classes and objects

A **class** is a blueprint. An **object** is a thing built from that
blueprint. One blueprint, as many objects as you want, and each object keeps
its own data.

```java
public class Counter {
    private int count;              // field: every Counter has its own count
    private String label;

    public Counter(String label) {  // constructor: runs when you say "new"
        this.label = label;
        this.count = 0;
    }

    public void click() {           // method: something a Counter can do
        count = count + 1;
    }

    public int getCount() {         // "getter": lets others read the count
        return count;
    }
}
```

Using it:

```java
Counter laps = new Counter("laps");
Counter goals = new Counter("goals");
laps.click();
laps.click();
goals.click();
System.out.println(laps.getCount());    // 2
System.out.println(goals.getCount());   // 1
```

Things to notice:

- Fields are `private`. Code outside the class cannot touch `count` directly.
  It has to go through the methods. That lets the class protect its own data
  (nobody can set the count to -500).
- These methods are NOT `static`. They belong to one particular object and
  can use that object's fields.
- `this.label = label` means "store the parameter `label` in *my* field
  `label`".

## Variables hold references

A variable does not hold the object itself. It holds a **reference**: an
arrow pointing at the object.

```java
Counter a = new Counter("a");
Counter b = a;        // no new object! b points at the SAME Counter as a
b.click();
System.out.println(a.getCount());   // what does this print? Why?
```

## Your job

On a robot, every motor has a name and a power between -1.0 (full reverse)
and 1.0 (full forward). Build that.

Open `lessons/03-classes/Motor.java` and write a `Motor` class with:

1. A constructor `Motor(String name)`. A new motor has power 0.0.
2. `String getName()`
3. `double getPower()`
4. `void setPower(double power)`. A motor cannot go past its limits:
   anything above 1.0 becomes 1.0, anything below -1.0 becomes -1.0.
5. `boolean isRunning()`: true whenever the power is not 0.
6. `void stop()`: sets the power back to 0.

All fields must be `private`, and none of them `static`.

`Main.java` has a small demo in comments. Once your Motor compiles, remove
the `//` marks and run `mise run try 3`. Before you run it, guess what the
last line will print.

## Look it up

Classes in the official Java tutorial:
  https://docs.oracle.com/javase/tutorial/java/javaOO/classes.html

There is also a finished example of a small class in `src/Classes.java`
(`mise run example Classes`).

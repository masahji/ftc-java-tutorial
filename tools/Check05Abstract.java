import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Check05Abstract implements Checker.Suite {
    public void run(Checker c) {
        Student s = c.s;

        c.section("Shape becomes abstract");
        c.check("Shape is an abstract class", () ->
                c.yes(Modifier.isAbstract(s.cls("Shape").getModifiers()) && !s.cls("Shape").isInterface(),
                        "Shape is not abstract yet."));
        for (String name : List.of("area", "perimeter")) {
            c.check("Shape." + name + "() is abstract (no body)", () -> {
                Method m;
                try {
                    m = s.cls("Shape").getDeclaredMethod(name);
                } catch (NoSuchMethodException e) {
                    throw new Checker.Fail("Shape has no " + name + "() method.");
                }
                c.yes(Modifier.isAbstract(m.getModifiers()), "Shape." + name + "() still has a body.");
            });
        }
        c.check("Rectangle and Square still work", () -> {
            c.eq("new Rectangle(3, 4).area()", 12.0, s.call(s.make("Rectangle", 3.0, 4.0), "area"));
            c.eq("new Square(5).describe()", "Square with area 25.0", s.call(s.make("Square", 5.0), "describe"));
        });

        c.section("Triangle");
        c.gate("there is a Triangle class", () -> s.cls("Triangle"));
        c.check("Triangle extends Shape directly", () ->
                c.eq("Triangle's parent class", "Shape", s.cls("Triangle").getSuperclass().getSimpleName()));
        c.gate("new Triangle(3, 4, 5) works", () -> s.make("Triangle", 3.0, 4.0, 5.0));
        c.check("getName() is \"Triangle\"", () ->
                c.eq("new Triangle(3, 4, 5).getName()", "Triangle",
                        s.call(s.make("Triangle", 3.0, 4.0, 5.0), "getName")));
        c.check("perimeter()", () -> {
            c.eq("new Triangle(3, 4, 5).perimeter()", 12.0, s.call(s.make("Triangle", 3.0, 4.0, 5.0), "perimeter"));
            c.eq("new Triangle(5, 5, 6).perimeter()", 16.0, s.call(s.make("Triangle", 5.0, 5.0, 6.0), "perimeter"));
        });
        c.check("area()", () -> {
            c.eq("new Triangle(3, 4, 5).area()", 6.0, s.call(s.make("Triangle", 3.0, 4.0, 5.0), "area"));
            c.eq("new Triangle(5, 5, 6).area()", 12.0, s.call(s.make("Triangle", 5.0, 5.0, 6.0), "area"));
            c.eq("new Triangle(13, 14, 15).area()", 84.0, s.call(s.make("Triangle", 13.0, 14.0, 15.0), "area"));
        });

        c.section("Shapes: one list, many kinds of shape");
        c.gate("there is a Shapes class", () -> s.cls("Shapes"));
        c.check("totalArea of [Rectangle 3x4, Square 5, Triangle 3-4-5] is 43.0", () ->
                c.eq("Shapes.totalArea(shapes)", 43.0, s.callStatic("Shapes", "totalArea", mixed(s))));
        c.check("totalArea of an empty list is 0.0", () ->
                c.eq("Shapes.totalArea(emptyList)", 0.0, s.callStatic("Shapes", "totalArea", new ArrayList<>())));
        c.check("largest returns the shape with the biggest area", () -> {
            List<Object> shapes = mixed(s);
            Object result = s.callStatic("Shapes", "largest", shapes);
            c.yes(result == shapes.get(1), "From [Rectangle 3x4, Square 5, Triangle 3-4-5] I expected the Square,\n"
                    + "but got: " + describe(s, result));
        });
        c.check("largest works when the biggest shape is last", () -> {
            List<Object> shapes = mixed(s);
            Object big = s.make("Triangle", 13.0, 14.0, 15.0);
            shapes.add(big);
            Object result = s.callStatic("Shapes", "largest", shapes);
            c.yes(result == big, "I added a Triangle 13-14-15 (area 84) to the end and expected it back,\n"
                    + "but got: " + describe(s, result));
        });
        c.check("largest of a list with one shape is that shape", () -> {
            List<Object> shapes = new ArrayList<>();
            shapes.add(s.make("Square", 2.0));
            c.yes(s.callStatic("Shapes", "largest", shapes) == shapes.get(0), "Expected the only shape in the list.");
        });
    }

    static List<Object> mixed(Student s) throws Throwable {
        List<Object> shapes = new ArrayList<>();
        shapes.add(s.make("Rectangle", 3.0, 4.0));
        shapes.add(s.make("Square", 5.0));
        shapes.add(s.make("Triangle", 3.0, 4.0, 5.0));
        return shapes;
    }

    static String describe(Student s, Object shape) throws Throwable {
        return shape == null ? "null" : String.valueOf(s.call(shape, "describe"));
    }
}

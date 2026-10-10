import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Check06Interfaces implements Checker.Suite {
    public void run(Checker c) {
        Student s = c.s;

        c.section("Measurable");
        c.gate("there is a Measurable interface", () ->
                c.yes(s.cls("Measurable").isInterface(), "Measurable exists, but it is a class. Make it an interface."));
        c.check("Measurable declares  double area()", () -> {
            try {
                c.eq("the return type of area()", "double",
                        s.cls("Measurable").getMethod("area").getReturnType().getSimpleName());
            } catch (NoSuchMethodException e) {
                throw new Checker.Fail("Measurable has no area() method.");
            }
        });
        c.check("Shape implements Measurable", () ->
                c.yes(s.cls("Measurable").isAssignableFrom(s.cls("Shape")),
                        "Shape does not say  implements Measurable  yet."));

        c.section("Tile");
        c.gate("there is a Tile class", () -> s.cls("Tile"));
        c.check("Tile is NOT a Shape", () ->
                c.yes(!s.cls("Shape").isAssignableFrom(s.cls("Tile")),
                        "Tile extends Shape. For this lesson, Tile must not extend anything."));
        c.check("Tile implements Measurable", () ->
                c.yes(s.cls("Measurable").isAssignableFrom(s.cls("Tile")),
                        "Tile does not say  implements Measurable  yet."));
        c.check("new Tile(24).area() is 576.0", () ->
                c.eq("new Tile(24).area()", 576.0, s.call(s.make("Tile", 24.0), "area")));

        c.section("Measure.total: works on anything Measurable");
        c.gate("there is a Measure class", () -> s.cls("Measure"));
        c.check("total of [Rectangle 3x4, Tile 2, Square 5] is 41.0", () -> {
            List<Object> things = new ArrayList<>();
            things.add(s.make("Rectangle", 3.0, 4.0));
            things.add(s.make("Tile", 2.0));
            things.add(s.make("Square", 5.0));
            c.eq("Measure.total(things)", 41.0, s.callStatic("Measure", "total", things));
        });
        c.check("total of an empty list is 0.0", () ->
                c.eq("Measure.total(emptyList)", 0.0, s.callStatic("Measure", "total", new ArrayList<>())));
        c.check("total works on a Measurable the checker just invented", () -> {
            // The checker builds its own object that implements YOUR interface.
            // Your Measure class has never seen it, and it still has to work.
            Class<?> measurable = s.cls("Measurable");
            Object mystery = Proxy.newProxyInstance(s.loader, new Class<?>[] {measurable}, (self, method, args) ->
                    switch (method.getName()) {
                        case "area" -> 100.0;
                        case "equals" -> self == args[0];
                        case "hashCode" -> 7;
                        default -> "MysteryThing";
                    });
            List<Object> things = new ArrayList<>();
            things.add(mystery);
            things.add(s.make("Tile", 2.0));
            c.eq("Measure.total([MysteryThing with area 100, Tile 2])", 104.0,
                    s.callStatic("Measure", "total", things));
        });

        c.section("Comparable: teaching Java how to sort your shapes");
        c.gate("Shape implements Comparable<Shape>", () ->
                c.yes(Comparable.class.isAssignableFrom(s.cls("Shape")),
                        "Shape does not say  implements Comparable<Shape>  yet."));
        c.check("a smaller shape compareTo a bigger shape is negative", () -> {
            Object result = s.call(s.make("Square", 2.0), "compareTo", s.make("Rectangle", 3.0, 4.0));
            c.yes(Checker.number("compareTo", result) < 0, "Square 2 (area 4) compareTo Rectangle 3x4 (area 12)\n"
                    + "should be negative, but it was " + result);
        });
        c.check("a bigger shape compareTo a smaller shape is positive", () -> {
            Object result = s.call(s.make("Rectangle", 3.0, 4.0), "compareTo", s.make("Square", 2.0));
            c.yes(Checker.number("compareTo", result) > 0, "Rectangle 3x4 (area 12) compareTo Square 2 (area 4)\n"
                    + "should be positive, but it was " + result);
        });
        c.check("shapes with equal areas compare as 0", () ->
                c.eq("Rectangle 2x8 compareTo Square 4 (both area 16)", 0,
                        s.call(s.make("Rectangle", 2.0, 8.0), "compareTo", s.make("Square", 4.0))));
        c.check("areas that differ by less than 1 still compare correctly", () -> {
            Object result = s.call(s.make("Square", 1.0), "compareTo", s.make("Rectangle", 1.0, 1.5));
            c.yes(Checker.number("compareTo", result) < 0, "Square 1 (area 1.0) compareTo Rectangle 1x1.5 (area 1.5)\n"
                    + "should be negative, but it was " + result + "\n"
                    + "(Careful: turning 1.0 - 1.5 into an int gives 0.)");
        });
        c.check("Collections.sort puts shapes in order, smallest area first", () -> {
            List<Object> shapes = new ArrayList<>();
            shapes.add(s.make("Square", 5.0));
            shapes.add(s.make("Triangle", 3.0, 4.0, 5.0));
            shapes.add(s.make("Rectangle", 3.0, 4.0));
            shapes.add(s.make("Square", 1.0));
            sort(shapes);
            List<Object> areas = new ArrayList<>();
            for (Object shape : shapes) {
                areas.add(s.call(shape, "area"));
            }
            c.eq("the areas after Collections.sort(shapes)", List.of(1.0, 6.0, 12.0, 25.0), areas);
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void sort(List shapes) {
        Collections.sort(shapes);
    }
}

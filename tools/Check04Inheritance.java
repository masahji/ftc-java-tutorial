import java.lang.reflect.Method;

public class Check04Inheritance implements Checker.Suite {
    public void run(Checker c) {
        Student s = c.s;

        c.section("Rectangle");
        c.gate("there is a Rectangle class", () -> s.cls("Rectangle"));
        c.check("Rectangle extends Shape", () ->
                c.eq("Rectangle's parent class", "Shape", s.cls("Rectangle").getSuperclass().getSimpleName()));
        c.gate("new Rectangle(3, 4) works", () -> s.make("Rectangle", 3.0, 4.0));
        c.check("getWidth() and getHeight()", () -> {
            Object r = s.make("Rectangle", 3.0, 4.0);
            c.eq("new Rectangle(3, 4).getWidth()", 3.0, s.call(r, "getWidth"));
            c.eq("new Rectangle(3, 4).getHeight()", 4.0, s.call(r, "getHeight"));
        });
        c.check("area()", () -> {
            c.eq("new Rectangle(3, 4).area()", 12.0, s.call(s.make("Rectangle", 3.0, 4.0), "area"));
            c.eq("new Rectangle(2.5, 2).area()", 5.0, s.call(s.make("Rectangle", 2.5, 2.0), "area"));
        });
        c.check("perimeter()", () -> {
            c.eq("new Rectangle(3, 4).perimeter()", 14.0, s.call(s.make("Rectangle", 3.0, 4.0), "perimeter"));
            c.eq("new Rectangle(10, 1).perimeter()", 22.0, s.call(s.make("Rectangle", 10.0, 1.0), "perimeter"));
        });
        c.check("getName() is \"Rectangle\"", () ->
                c.eq("new Rectangle(3, 4).getName()", "Rectangle", s.call(s.make("Rectangle", 3.0, 4.0), "getName")));
        c.check("describe() (inherited from Shape) uses Rectangle's area", () ->
                c.eq("new Rectangle(3, 4).describe()", "Rectangle with area 12.0",
                        s.call(s.make("Rectangle", 3.0, 4.0), "describe")));

        c.section("Square");
        c.gate("there is a Square class", () -> s.cls("Square"));
        c.check("Square extends Rectangle", () ->
                c.eq("Square's parent class", "Rectangle", s.cls("Square").getSuperclass().getSimpleName()));
        c.gate("new Square(5) works", () -> s.make("Square", 5.0));
        c.check("a Square's width and height are both its side", () -> {
            Object sq = s.make("Square", 5.0);
            c.eq("new Square(5).getWidth()", 5.0, s.call(sq, "getWidth"));
            c.eq("new Square(5).getHeight()", 5.0, s.call(sq, "getHeight"));
        });
        c.check("area() and perimeter()", () -> {
            Object sq = s.make("Square", 5.0);
            c.eq("new Square(5).area()", 25.0, s.call(sq, "area"));
            c.eq("new Square(5).perimeter()", 20.0, s.call(sq, "perimeter"));
        });
        c.check("Square does NOT write its own area() or perimeter()", () -> {
            for (Method m : s.cls("Square").getDeclaredMethods()) {
                c.yes(!m.getName().equals("area") && !m.getName().equals("perimeter"),
                        "Square has its own " + m.getName() + "() method. Delete it!\n"
                                + "A Square is a Rectangle, so Rectangle's " + m.getName() + "() already works.");
            }
        });
        c.check("getName() is \"Square\"", () ->
                c.eq("new Square(5).getName()", "Square", s.call(s.make("Square", 5.0), "getName")));
        c.check("describe()", () ->
                c.eq("new Square(5).describe()", "Square with area 25.0", s.call(s.make("Square", 5.0), "describe")));
        c.check("a Rectangle is still called \"Rectangle\"", () ->
                c.eq("new Rectangle(1, 2).getName()", "Rectangle", s.call(s.make("Rectangle", 1.0, 2.0), "getName")));
    }
}

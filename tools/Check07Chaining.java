public class Check07Chaining implements Checker.Suite {
    public void run(Checker c) {
        Student s = c.s;

        c.section("Color");
        c.gate("there is a Color class", () -> s.cls("Color"));
        c.gate("new Color(255, 136, 0) works", () -> s.make("Color", 255, 136, 0));
        c.check("getRed(), getGreen(), getBlue()", () -> {
            Object color = s.make("Color", 255, 136, 0);
            c.eq("new Color(255, 136, 0).getRed()", 255, s.call(color, "getRed"));
            c.eq("new Color(255, 136, 0).getGreen()", 136, s.call(color, "getGreen"));
            c.eq("new Color(255, 136, 0).getBlue()", 0, s.call(color, "getBlue"));
        });

        c.section("toHex");
        hex(c, 255, 136, 0, "#FF8800");
        hex(c, 255, 255, 255, "#FFFFFF");
        hex(c, 0, 0, 0, "#000000");
        hex(c, 5, 10, 15, "#050A0F");
        hex(c, 18, 171, 9, "#12AB09");

        c.section("with...: each one returns a NEW Color");
        c.check("withRed(200) returns a Color whose red is 200", () -> {
            Object changed = s.call(s.make("Color", 1, 2, 3), "withRed", 200);
            c.eq("new Color(1, 2, 3).withRed(200).toHex()", "#C80203", s.call(changed, "toHex"));
        });
        c.check("withGreen(200) returns a Color whose green is 200", () -> {
            Object changed = s.call(s.make("Color", 1, 2, 3), "withGreen", 200);
            c.eq("new Color(1, 2, 3).withGreen(200).toHex()", "#01C803", s.call(changed, "toHex"));
        });
        c.check("withBlue(200) returns a Color whose blue is 200", () -> {
            Object changed = s.call(s.make("Color", 1, 2, 3), "withBlue", 200);
            c.eq("new Color(1, 2, 3).withBlue(200).toHex()", "#0102C8", s.call(changed, "toHex"));
        });
        c.check("withRed does not change the original Color", () -> {
            Object original = s.make("Color", 1, 2, 3);
            Object changed = s.call(original, "withRed", 200);
            c.yes(changed != original, "withRed returned the same object. It should return a new Color.");
            c.eq("the original's getRed() after calling withRed(200) on it", 1, s.call(original, "getRed"));
        });
        c.check("chain: new Color(0, 0, 0).withRed(255).withGreen(136).toHex()", () -> {
            Object step1 = s.call(s.make("Color", 0, 0, 0), "withRed", 255);
            Object step2 = s.call(step1, "withGreen", 136);
            c.eq("new Color(0, 0, 0).withRed(255).withGreen(136).toHex()", "#FF8800", s.call(step2, "toHex"));
        });

        c.section("mix");
        c.check("red mixed with blue", () -> {
            Object mixed = s.call(s.make("Color", 255, 0, 0), "mix", s.make("Color", 0, 0, 255));
            c.eq("new Color(255, 0, 0).mix(new Color(0, 0, 255)).toHex()", "#7F007F", s.call(mixed, "toHex"));
        });
        c.check("mixing leaves both original colors alone", () -> {
            Object red = s.make("Color", 255, 0, 0);
            Object blue = s.make("Color", 0, 0, 255);
            s.call(red, "mix", blue);
            c.eq("red.toHex() after red.mix(blue)", "#FF0000", s.call(red, "toHex"));
            c.eq("blue.toHex() after red.mix(blue)", "#0000FF", s.call(blue, "toHex"));
        });
        c.check("chain: white.mix(black).mix(black).toHex()", () -> {
            Object black = s.make("Color", 0, 0, 0);
            Object once = s.call(s.make("Color", 255, 255, 255), "mix", black);
            Object twice = s.call(once, "mix", black);
            c.eq("new Color(255, 255, 255).mix(black).mix(black).toHex()", "#3F3F3F", s.call(twice, "toHex"));
        });

        c.section("Painting shapes");
        c.check("paint(color) returns the same shape, so calls can be chained", () -> {
            Object square = s.make("Square", 2.0);
            Object result = s.call(square, "paint", s.make("Color", 255, 136, 0));
            c.yes(result == square, "square.paint(color) should return the square itself (this), but it returned "
                    + (result == null ? "null" : "a different object") + ".");
        });
        c.check("getColor() returns the color the shape was painted", () -> {
            Object color = s.make("Color", 255, 136, 0);
            Object triangle = s.make("Triangle", 3.0, 4.0, 5.0);
            s.call(triangle, "paint", color);
            c.yes(s.call(triangle, "getColor") == color, "getColor() should return the same Color given to paint.");
        });
        c.check("chain: new Square(2).paint(orange).getColor().toHex()", () -> {
            Object painted = s.call(s.make("Square", 2.0), "paint", s.make("Color", 255, 136, 0));
            c.eq("new Square(2).paint(new Color(255, 136, 0)).getColor().toHex()", "#FF8800",
                    s.call(s.call(painted, "getColor"), "toHex"));
        });
        c.check("chain: new Rectangle(3, 4).paint(orange).describe()", () -> {
            Object painted = s.call(s.make("Rectangle", 3.0, 4.0), "paint", s.make("Color", 255, 136, 0));
            c.eq("new Rectangle(3, 4).paint(orange).describe()", "Rectangle with area 12.0",
                    s.call(painted, "describe"));
        });
    }

    private static void hex(Checker c, int r, int g, int b, String expected) {
        String call = "new Color(" + r + ", " + g + ", " + b + ").toHex()";
        c.check(call + " returns \"" + expected + "\"", () ->
                c.eq(call, expected, c.s.call(c.s.make("Color", r, g, b), "toHex")));
    }
}

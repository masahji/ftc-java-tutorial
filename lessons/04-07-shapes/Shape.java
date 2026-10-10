// The general idea of a shape. Rectangle, Square and Triangle will build on it.
public class Shape {
    private String name;

    public Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // A general Shape has no idea how big it is. Subclasses override these.
    public double area() {
        return 0;
    }

    public double perimeter() {
        return 0;
    }

    public String describe() {
        return getName() + " with area " + area();
    }
}

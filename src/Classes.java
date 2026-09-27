public class Classes {
    // A class bundles data (fields) with behavior (methods)
    static class Dog {
        private final String name;
        private int tricks;

        Dog(String name) {
            this.name = name;
        }

        void learnTrick() {
            tricks++;
        }

        String describe() {
            return name + " knows " + tricks + " trick(s)";
        }
    }

    // A record is a compact class for plain data
    record Point(int x, int y) {}

    public static void main(String[] args) {
        Dog rex = new Dog("Rex");
        rex.learnTrick();
        rex.learnTrick();
        System.out.println(rex.describe());

        Point p = new Point(3, 4);
        System.out.println(p + " -> x=" + p.x());
    }
}

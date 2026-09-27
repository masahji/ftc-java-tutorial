public class Methods {
    static int add(int a, int b) {
        return a + b;
    }

    static String greet(String name) {
        return "Hi, " + name + "!";
    }

    static boolean isEven(int n) {
        return n % 2 == 0;
    }

    public static void main(String[] args) {
        System.out.println(add(2, 3));
        System.out.println(greet("Grace"));
        System.out.println("4 is even? " + isEven(4));
    }
}

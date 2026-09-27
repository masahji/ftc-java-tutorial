public class Variables {
    public static void main(String[] args) {
        // Primitive types hold simple values
        int age = 30;
        double price = 19.99;
        boolean isJavaFun = true;
        char grade = 'A';

        // String is an object, not a primitive
        String name = "Ada";

        // var lets the compiler infer the type
        var count = 3;

        System.out.println(name + " is " + age + " years old");
        System.out.println("Price: " + price + ", grade: " + grade);
        System.out.println("Java is fun? " + isJavaFun);
        System.out.println("count * 2 = " + (count * 2));
    }
}

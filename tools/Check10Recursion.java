import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Check10Recursion implements Checker.Suite {
    static final String CLASS = "Recursion";

    public void run(Checker c) {
        Student s = c.s;
        c.gate("there is a Recursion class", () -> s.cls(CLASS));
        c.check("Recursion.java has no loops (no for, no while)", () -> {
            Matcher loop = Pattern.compile("\\b(for|while)\\b").matcher(s.code("Recursion.java"));
            if (loop.find()) {
                throw new Checker.Fail("I found the word \"" + loop.group() + "\" in Recursion.java.\n"
                        + "Every method in this lesson has to work by calling itself. No loops allowed!");
            }
        });

        c.section("factorial");
        c.returns(CLASS, "factorial", 1, 0);
        c.returns(CLASS, "factorial", 1, 1);
        c.returns(CLASS, "factorial", 120, 5);
        c.returns(CLASS, "factorial", 3628800, 10);
        c.returns(CLASS, "factorial", 1307674368000L, 15);

        c.section("sumDigits");
        c.returns(CLASS, "sumDigits", 0, 0);
        c.returns(CLASS, "sumDigits", 7, 7);
        c.returns(CLASS, "sumDigits", 10, 1234);
        c.returns(CLASS, "sumDigits", 45, 99999);
        c.returns(CLASS, "sumDigits", 1, 100000);

        c.section("power");
        c.returns(CLASS, "power", 1, 3, 0);
        c.returns(CLASS, "power", 5, 5, 1);
        c.returns(CLASS, "power", 343, 7, 3);
        c.returns(CLASS, "power", 1024, 2, 10);
        c.returns(CLASS, "power", 1099511627776L, 2, 40);

        c.section("fib");
        c.returns(CLASS, "fib", 0, 0);
        c.returns(CLASS, "fib", 1, 1);
        c.returns(CLASS, "fib", 1, 2);
        c.returns(CLASS, "fib", 55, 10);
        c.returns(CLASS, "fib", 6765, 20);

        c.section("reverse");
        c.returns(CLASS, "reverse", "", "");
        c.returns(CLASS, "reverse", "a", "a");
        c.returns(CLASS, "reverse", "tobor", "robot");
        c.returns(CLASS, "reverse", "avaJ olleH", "Hello Java");

        c.section("isPalindrome");
        c.returns(CLASS, "isPalindrome", true, "racecar");
        c.returns(CLASS, "isPalindrome", true, "noon");
        c.returns(CLASS, "isPalindrome", false, "robot");
        c.returns(CLASS, "isPalindrome", false, "ab");
        c.returns(CLASS, "isPalindrome", true, "x");
        c.returns(CLASS, "isPalindrome", true, "");
        c.returns(CLASS, "isPalindrome", false, "abca");
    }
}

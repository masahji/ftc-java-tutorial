// The rule for this file: no loops. Every method must call itself.
public class Recursion {

    // n! = n * (n-1) * ... * 2 * 1, and 0! is 1.
    //   factorial(5) -> 120
    static long factorial(int n) {
        return 0; // TODO: replace this line
    }

    // The digits of n added together. n is never negative.
    //   sumDigits(1234) -> 10
    static int sumDigits(int n) {
        return -1; // TODO: replace this line
    }

    // base multiplied by itself exponent times. exponent is never negative.
    //   power(2, 10) -> 1024
    //   power(3, 0)  -> 1
    static long power(int base, int exponent) {
        return 0; // TODO: replace this line
    }

    // The nth Fibonacci number: 0, 1, 1, 2, 3, 5, 8, 13, ...
    //   fib(0) -> 0,  fib(1) -> 1,  fib(6) -> 8
    static long fib(int n) {
        return -1; // TODO: replace this line
    }

    // The text backward.
    //   reverse("robot") -> "tobor"
    static String reverse(String text) {
        return null; // TODO: replace this line
    }

    // True if the text reads the same forward and backward.
    //   isPalindrome("racecar") -> true
    //   isPalindrome("robot")   -> false
    static boolean isPalindrome(String text) {
        return false; // TODO: replace this line
    }
}

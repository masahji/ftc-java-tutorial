import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Runs the checks for one lesson and prints PASS / FAIL for each.
 *
 * Students: this is the grader. Reading it will tell you WHAT is being
 * tested, but not HOW to write the code. That part is yours.
 */
public class Checker {
    /** One lesson's worth of checks. */
    public interface Suite {
        void run(Checker c) throws Throwable;
    }

    /** The body of a single check. Throw Fail (or anything) to fail it. */
    public interface Step {
        void run() throws Throwable;
    }

    public static class Fail extends RuntimeException {
        public Fail(String message) {
            super(message);
        }
    }

    /** Thrown to skip the rest of a lesson once a required check fails. */
    static class Stop extends RuntimeException {
    }

    private static final long TIMEOUT_MILLIS = 5000;

    public final Student s;
    private final PrintStream out;
    private final boolean color;
    private final ByteArrayOutputStream captured = new ByteArrayOutputStream();
    int passed;
    int failed;

    Checker(Student s, PrintStream out, boolean color) {
        this.s = s;
        this.out = out;
        this.color = color;
    }

    // ---- running checks ---------------------------------------------------

    /** Runs one check. Returns true if it passed. */
    public boolean check(String name, Step step) {
        captured.reset();
        PrintStream realOut = System.out;
        System.setOut(new PrintStream(captured, true));
        Throwable[] problem = new Throwable[1];
        Thread thread = new Thread(() -> {
            try {
                step.run();
            } catch (Throwable e) {
                problem[0] = e;
            }
        }, "check");
        thread.setDaemon(true);
        thread.start();
        try {
            thread.join(TIMEOUT_MILLIS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.setOut(realOut);

        if (thread.isAlive()) {
            report(name, "Your code ran for more than 5 seconds, so I stopped waiting.\n"
                    + "Is there a loop (or a recursive method) that never ends?");
            throw new Stop();
        }
        if (problem[0] == null) {
            passed++;
            out.println("  " + paint("32", "[PASS]") + " " + name);
            return true;
        }
        report(name, explain(problem[0]));
        return false;
    }

    /** Like check, but if it fails the rest of the lesson is skipped. */
    public void gate(String name, Step step) {
        if (!check(name, step)) {
            out.println();
            out.println("  Fix that one first. The remaining checks depend on it.");
            throw new Stop();
        }
    }

    /** Checks that a static method returns the expected value. */
    public void returns(String className, String method, Object expected, Object... args) {
        String call = method + "(" + showArgs(args) + ")";
        check(call + " returns " + show(expected),
                () -> eq(call, expected, s.callStatic(className, method, args)));
    }

    public void section(String title) {
        if (passed + failed > 0) {
            out.println();
        }
        out.println(paint("1", title));
    }

    private void report(String name, String why) {
        failed++;
        out.println("  " + paint("31", "[FAIL]") + " " + name);
        for (String line : why.split("\n")) {
            out.println("         " + line);
        }
        String printed = printed();
        if (!printed.isBlank()) {
            out.println("         While running, your code printed:");
            String[] lines = printed.split("\n");
            for (int i = 0; i < Math.min(lines.length, 8); i++) {
                out.println("           | " + lines[i]);
            }
            if (lines.length > 8) {
                out.println("           | ... (" + (lines.length - 8) + " more lines)");
            }
        }
    }

    private String explain(Throwable e) {
        if (e instanceof Fail) {
            return e.getMessage();
        }
        if (e instanceof StackOverflowError) {
            return "StackOverflowError: a method kept calling itself and never stopped.\n"
                    + "Check your base case." + where(e);
        }
        return "Your code crashed with " + e + where(e);
    }

    /** Finds the first line of the student's own code in a stack trace. */
    private String where(Throwable e) {
        for (StackTraceElement frame : e.getStackTrace()) {
            String file = frame.getFileName();
            if (file != null && Files.exists(Path.of(s.dir, file))) {
                return "\n  at " + frame.getMethodName() + " (" + file + " line " + frame.getLineNumber() + ")";
            }
        }
        return "";
    }

    // ---- things a check can look at ---------------------------------------

    /** Everything the student's code has printed during the current check. */
    public String printed() {
        return captured.toString().replace("\r\n", "\n");
    }

    /** Runs a class's main method and returns the lines it printed. */
    public List<String> runMain(String className) throws Throwable {
        captured.reset();
        s.callStatic(className, "main", new Object[] {new String[0]});
        List<String> lines = new ArrayList<>();
        for (String line : printed().split("\n")) {
            lines.add(line.stripTrailing());
        }
        while (!lines.isEmpty() && lines.get(lines.size() - 1).isEmpty()) {
            lines.remove(lines.size() - 1);
        }
        return lines;
    }

    // ---- assertions --------------------------------------------------------

    public void yes(boolean condition, String messageIfNot) {
        if (!condition) {
            throw new Fail(messageIfNot);
        }
    }

    public void eq(String what, Object expected, Object actual) {
        if (!same(expected, actual)) {
            throw new Fail(what + "\n  expected: " + show(expected) + "\n  but got:  " + show(actual));
        }
    }

    private static boolean same(Object expected, Object actual) {
        if (isWhole(expected) && isWhole(actual)) {
            return ((Number) expected).longValue() == ((Number) actual).longValue();
        }
        if (expected instanceof Number e && actual instanceof Number a) {
            double x = e.doubleValue();
            double y = a.doubleValue();
            return Math.abs(x - y) <= 1e-6 * Math.max(1, Math.abs(x));
        }
        return Objects.equals(expected, actual);
    }

    private static boolean isWhole(Object value) {
        return value instanceof Integer || value instanceof Long || value instanceof Short || value instanceof Byte;
    }

    public static double number(String what, Object value) {
        if (value instanceof Number n) {
            return n.doubleValue();
        }
        throw new Fail(what + " should be a number, but it was " + show(value));
    }

    public static String show(Object value) {
        if (value instanceof String text) {
            return "\"" + text + "\"";
        }
        if (value instanceof Object[] array) {
            return Arrays.toString(array);
        }
        return String.valueOf(value);
    }

    private static String showArgs(Object[] args) {
        StringBuilder text = new StringBuilder();
        for (Object arg : args) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(show(arg));
        }
        return text.toString();
    }

    String paint(String code, String text) {
        return color ? "\u001B[" + code + "m" + text + "\u001B[0m" : text;
    }
}

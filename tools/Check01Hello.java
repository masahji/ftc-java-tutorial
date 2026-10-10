import java.util.List;

public class Check01Hello implements Checker.Suite {
    public void run(Checker c) {
        Student s = c.s;
        c.gate("there is a Hello class", () -> s.cls("Hello"));

        c.check("main prints Hello, World! on the first line", () -> {
            List<String> lines = c.runMain("Hello");
            c.yes(!lines.isEmpty(), "main ran, but it didn't print anything.");
            c.eq("The first line main prints", "Hello, World!", lines.get(0));
        });
        c.returns("Hello", "greet", "Hello, Ada!", "Ada");
        c.returns("Hello", "greet", "Hello, Grace!", "Grace");
        c.returns("Hello", "shout", "HELLO, ADA!", "Ada");
        c.returns("Hello", "shout", "HELLO, ROBOT!", "robot");
        c.check("main prints a second line that comes from greet (with your own name)", () -> {
            List<String> lines = c.runMain("Hello");
            c.yes(lines.size() >= 2, "main only printed " + lines.size() + " line(s). Print what greet returns too.");
            String second = lines.get(1);
            c.yes(second.startsWith("Hello, ") && second.endsWith("!") && !second.equals("Hello, World!"),
                    "The second line should look like  Hello, <your name>!  but it was " + Checker.show(second));
        });
        c.check("main prints a third line that comes from shout", () -> {
            List<String> lines = c.runMain("Hello");
            c.yes(lines.size() >= 3, "main only printed " + lines.size() + " line(s). Print what shout returns too.");
            String third = lines.get(2);
            c.yes(third.startsWith("HELLO, ") && third.endsWith("!"),
                    "The third line should look like  HELLO, <YOUR NAME>!  but it was " + Checker.show(third));
        });
    }
}

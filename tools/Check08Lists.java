import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Check08Lists implements Checker.Suite {
    static final String CLASS = "ListAlgorithms";

    public void run(Checker c) {
        Student s = c.s;
        c.gate("there is a ListAlgorithms class", () -> s.cls(CLASS));

        c.section("indexOf");
        c.returns(CLASS, "indexOf", 0, list(10, 20, 30, 20), 10);
        c.returns(CLASS, "indexOf", 1, list(10, 20, 30, 20), 20);
        c.returns(CLASS, "indexOf", 2, list(10, 20, 30, 20), 30);
        c.returns(CLASS, "indexOf", -1, list(10, 20, 30, 20), 99);
        c.returns(CLASS, "indexOf", -1, list(), 5);

        c.section("countOf");
        c.returns(CLASS, "countOf", 3, list(1, 2, 2, 3, 2), 2);
        c.returns(CLASS, "countOf", 1, list(1, 2, 2, 3, 2), 3);
        c.returns(CLASS, "countOf", 0, list(1, 2, 2, 3, 2), 9);
        c.returns(CLASS, "countOf", 2, list(5000, 7000, 5000), 5000);

        c.section("largest");
        c.returns(CLASS, "largest", 9, list(3, 9, 2));
        c.returns(CLASS, "largest", 12, list(3, 9, 12));
        c.returns(CLASS, "largest", -2, list(-5, -2, -9));
        c.returns(CLASS, "largest", 4, list(4));

        c.section("average");
        c.returns(CLASS, "average", 2.5, list(1, 2, 3, 4));
        c.returns(CLASS, "average", 1.5, list(1, 2));
        c.returns(CLASS, "average", 10.0, list(10));
        c.returns(CLASS, "average", 0.0, list(-3, 3));

        c.section("reversed");
        c.returns(CLASS, "reversed", list(3, 2, 1), list(1, 2, 3));
        c.returns(CLASS, "reversed", list(7, 5, 5, 1), list(1, 5, 5, 7));
        c.returns(CLASS, "reversed", list(), list());
        c.check("reversed does not change the list it was given", () -> {
            List<Integer> original = list(1, 2, 3);
            Object result = s.callStatic(CLASS, "reversed", original);
            c.yes(result != original, "reversed returned the same list object. It should build a new list.");
            c.eq("the original list after calling reversed on it", list(1, 2, 3), original);
        });

        c.section("randomElement");
        c.returns(CLASS, "randomElement", 42, list(42));
        c.check("randomElement always returns something from the list", () -> {
            List<Integer> numbers = list(10, 20, 30, 40, 50, 60);
            for (int i = 0; i < 600; i++) {
                Object pick = s.callStatic(CLASS, "randomElement", numbers);
                c.yes(numbers.contains(pick), "randomElement([10, 20, 30, 40, 50, 60]) returned " + pick
                        + ", which is not in the list.");
            }
        });
        c.check("randomElement is actually random: every element shows up eventually", () -> {
            List<Integer> numbers = list(10, 20, 30, 40, 50, 60);
            Set<Object> seen = new HashSet<>();
            for (int i = 0; i < 600; i++) {
                seen.add(s.callStatic(CLASS, "randomElement", numbers));
            }
            List<Integer> never = new ArrayList<>(numbers);
            never.removeAll(seen);
            c.yes(never.isEmpty(), "I called randomElement([10, 20, 30, 40, 50, 60]) 600 times and never got: " + never);
        });
        c.check("randomElement does not change the list", () -> {
            List<Integer> numbers = list(1, 2, 3);
            s.callStatic(CLASS, "randomElement", numbers);
            c.eq("the list after calling randomElement on it", list(1, 2, 3), numbers);
        });
    }

    static List<Integer> list(Integer... values) {
        return new ArrayList<>(List.of(values));
    }
}

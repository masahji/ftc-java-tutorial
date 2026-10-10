import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Check11Hanoi implements Checker.Suite {
    public void run(Checker c) {
        Student s = c.s;
        c.gate("there is a Hanoi class", () -> s.cls("Hanoi"));
        c.check("Hanoi.java has no loops (no for, no while)", () ->
                c.yes(!s.code("Hanoi.java").matches("(?s).*\\b(for|while)\\b.*"),
                        "I found a loop in Hanoi.java. This one has to be solved with recursion."));
        c.check("1 disk: the only move is A->C", () ->
                c.eq("Hanoi.solve(1, \"A\", \"C\", \"B\")", List.of("A->C"), s.callStatic("Hanoi", "solve", 1, "A", "C", "B")));
        for (int disks : new int[] {2, 3, 4, 5, 10}) {
            c.check(disks + " disks from A to C in " + ((1 << disks) - 1) + " moves", () ->
                    play(c, disks, "A", "C", "B"));
        }
        c.check("3 disks from left to middle (the pegs can have any names)", () ->
                play(c, 3, "left", "middle", "right"));
        c.check("0 disks: nothing to move", () ->
                c.eq("Hanoi.solve(0, \"A\", \"C\", \"B\")", List.of(), s.callStatic("Hanoi", "solve", 0, "A", "C", "B")));
    }

    /** Plays the student's moves on a real set of pegs and checks every rule. */
    private static void play(Checker c, int disks, String from, String to, String spare) throws Throwable {
        String call = "Hanoi.solve(" + disks + ", \"" + from + "\", \"" + to + "\", \"" + spare + "\")";
        Object result = c.s.callStatic("Hanoi", "solve", disks, from, to, spare);
        c.yes(result instanceof List, call + " should return a List of moves, but returned " + result);
        List<?> moves = (List<?>) result;

        Map<String, Deque<Integer>> pegs = new HashMap<>();
        for (String peg : List.of(from, to, spare)) {
            pegs.put(peg, new ArrayDeque<>());
        }
        for (int disk = disks; disk >= 1; disk--) {
            pegs.get(from).push(disk);
        }
        int count = 0;
        for (Object move : moves) {
            count++;
            String where = "Move " + count + " of " + call + " was " + Checker.show(move) + ".\n";
            String[] parts = String.valueOf(move).split("->");
            c.yes(parts.length == 2, where + "A move should look like \"" + from + "->" + to + "\".");
            Deque<Integer> source = pegs.get(parts[0].trim());
            Deque<Integer> target = pegs.get(parts[1].trim());
            c.yes(source != null && target != null, where + "The pegs are named " + from + ", " + to + " and " + spare + ".");
            c.yes(!source.isEmpty(), where + "But peg " + parts[0].trim() + " is empty at that point.");
            c.yes(target.isEmpty() || target.peek() > source.peek(), where + "That puts disk " + source.peek()
                    + " on top of the smaller disk " + target.peek() + ".");
            target.push(source.pop());
        }
        c.yes(pegs.get(to).size() == disks, "After all " + count + " moves of " + call + ", peg " + to + " has "
                + pegs.get(to).size() + " of the " + disks + " disks.");
        int best = (1 << disks) - 1;
        c.yes(count == best, call + " solved it in " + count + " moves, but it can be done in " + best + ".");
    }
}

import java.util.List;

// Prints your moves so you can follow along with real coins.
// Run it with:  mise run try 11
public class Main {
    public static void main(String[] args) {
        int disks = 3;
        List<String> moves = Hanoi.solve(disks, "A", "C", "B");

        if (moves == null) {
            System.out.println("Hanoi.solve returned null. Time to write it!");
            return;
        }
        System.out.println(disks + " disks, " + moves.size() + " moves:");
        int number = 1;
        for (String move : moves) {
            System.out.println("  " + number + ". " + move);
            number++;
        }
    }
}

import java.util.List;

// A place to try your Frequency methods. Run it with:  mise run try 9
// Change anything you like in here. The checker does not look at this file.
public class Main {
    public static void main(String[] args) {
        List<Integer> rolls = List.of(4, 2, 6, 4, 1, 4, 6, 3);
        System.out.println("rolls = " + rolls);
        System.out.println("counts: " + Frequency.countOccurrences(rolls));
        System.out.println("most frequent: " + Frequency.mostFrequent(rolls));

        List<String> parts = List.of("servo", "sensor", "motor", "mount", "server", "motion", "senate");
        System.out.println("parts = " + parts);
        System.out.println("prefix counts: " + Frequency.countPrefixes(parts, 3));
        System.out.println("top 3 prefixes: " + Frequency.topPrefixes(parts, 3, 3));
    }
}

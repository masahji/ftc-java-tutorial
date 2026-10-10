import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Check09HashMaps implements Checker.Suite {
    static final String CLASS = "Frequency";

    public void run(Checker c) {
        Student s = c.s;
        c.gate("there is a Frequency class", () -> s.cls(CLASS));

        c.section("countOccurrences");
        c.returns(CLASS, "countOccurrences", Map.of(1, 2, 2, 3, 3, 1), Check08Lists.list(1, 2, 2, 3, 2, 1));
        c.returns(CLASS, "countOccurrences", Map.of(7, 1), Check08Lists.list(7));
        c.returns(CLASS, "countOccurrences", Map.of(1000, 2, 2000, 1), Check08Lists.list(1000, 2000, 1000));
        c.returns(CLASS, "countOccurrences", Map.of(), Check08Lists.list());

        c.section("mostFrequent");
        c.returns(CLASS, "mostFrequent", 3, Check08Lists.list(3, 1, 3, 2, 3, 1));
        c.returns(CLASS, "mostFrequent", 9, Check08Lists.list(5, 9, 9, 5, 9));
        c.returns(CLASS, "mostFrequent", 6, Check08Lists.list(4, 6, 6));
        c.returns(CLASS, "mostFrequent", 7, Check08Lists.list(7));
        c.returns(CLASS, "mostFrequent", -2, Check08Lists.list(-2, 8, -2, 8, -2));
        c.returns(CLASS, "mostFrequent", 1000, Check08Lists.list(1000, 2000, 1000, 3000, 1000, 2000));

        c.section("countPrefixes");
        c.returns(CLASS, "countPrefixes", Map.of("rob", 2, "roc", 1), words("robot", "robe", "rocket"), 3);
        c.returns(CLASS, "countPrefixes", Map.of("ro", 3, "to", 1), words("robot", "robe", "rocket", "to"), 2);
        c.check("countPrefixes skips words shorter than the prefix size", () ->
                c.eq("countPrefixes([\"robot\", \"to\", \"a\", \"robe\"], 3)", Map.of("rob", 2),
                        s.callStatic(CLASS, "countPrefixes", words("robot", "to", "a", "robe"), 3)));
        c.check("a word exactly as long as the prefix size counts", () ->
                c.eq("countPrefixes([\"arm\", \"army\", \"art\"], 3)", Map.of("arm", 2, "art", 1),
                        s.callStatic(CLASS, "countPrefixes", words("arm", "army", "art"), 3)));

        c.section("topPrefixes");
        List<String> plain = words("apple", "banana", "apply", "band", "carbon", "bandit",
                "application", "cart", "banner", "dog");
        c.check("top 3 prefixes of size 3 (no ties)", () ->
                c.eq("topPrefixes(" + plain + ", 3, 3)", List.of("ban", "app", "car"),
                        s.callStatic(CLASS, "topPrefixes", new ArrayList<>(plain), 3, 3)));
        c.check("top 1 prefix of size 3", () ->
                c.eq("topPrefixes(" + plain + ", 3, 1)", List.of("ban"),
                        s.callStatic(CLASS, "topPrefixes", new ArrayList<>(plain), 3, 1)));
        c.check("top 2 prefixes of size 1", () ->
                c.eq("topPrefixes(" + plain + ", 1, 2)", List.of("b", "a"),
                        s.callStatic(CLASS, "topPrefixes", new ArrayList<>(plain), 1, 2)));
        List<String> ties = words("the", "robot", "rocket", "road", "then", "robotics", "roar",
                "robe", "rock", "there", "to");
        c.check("ties are broken alphabetically", () ->
                c.eq("topPrefixes(" + ties + ", 3, 3)", List.of("rob", "the", "roa"),
                        s.callStatic(CLASS, "topPrefixes", new ArrayList<>(ties), 3, 3)));
        c.check("asking for more prefixes than exist returns all of them", () ->
                c.eq("topPrefixes([\"motor\", \"servo\", \"motion\"], 3, 5)", List.of("mot", "ser"),
                        s.callStatic(CLASS, "topPrefixes", words("motor", "servo", "motion"), 3, 5)));
        c.check("topPrefixes does not change the list of words", () -> {
            List<String> copy = new ArrayList<>(plain);
            s.callStatic(CLASS, "topPrefixes", copy, 3, 3);
            c.eq("the word list after calling topPrefixes", plain, copy);
        });
    }

    static List<String> words(String... words) {
        return new ArrayList<>(List.of(words));
    }
}

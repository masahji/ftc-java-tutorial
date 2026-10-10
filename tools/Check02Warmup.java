public class Check02Warmup implements Checker.Suite {
    public void run(Checker c) {
        c.gate("there is a Warmup class", () -> c.s.cls("Warmup"));

        c.section("square");
        c.returns("Warmup", "square", 16, 4);
        c.returns("Warmup", "square", 9, -3);
        c.returns("Warmup", "square", 0, 0);

        c.section("isEven");
        c.returns("Warmup", "isEven", true, 10);
        c.returns("Warmup", "isEven", false, 7);
        c.returns("Warmup", "isEven", true, 0);
        c.returns("Warmup", "isEven", false, -3);

        c.section("largest");
        c.returns("Warmup", "largest", 9, 9, 2, 5);
        c.returns("Warmup", "largest", 9, 2, 9, 5);
        c.returns("Warmup", "largest", 9, 2, 5, 9);
        c.returns("Warmup", "largest", -1, -4, -1, -8);
        c.returns("Warmup", "largest", 7, 7, 7, 7);

        c.section("sumTo");
        c.returns("Warmup", "sumTo", 1, 1);
        c.returns("Warmup", "sumTo", 15, 5);
        c.returns("Warmup", "sumTo", 5050, 100);
        c.returns("Warmup", "sumTo", 0, 0);

        c.section("countVowels");
        c.returns("Warmup", "countVowels", 2, "robot");
        c.returns("Warmup", "countVowels", 0, "rhythm");
        c.returns("Warmup", "countVowels", 5, "AEIOU");
        c.returns("Warmup", "countVowels", 7, "Axiomatic Robots");
        c.returns("Warmup", "countVowels", 0, "");
    }
}

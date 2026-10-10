import java.util.List;

// A place to try your ListAlgorithms. Run it with:  mise run try 8
// Change anything you like in here. The checker does not look at this file.
public class Main {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(4, 8, 15, 16, 23, 42, 8);

        System.out.println("numbers = " + numbers);
        System.out.println("indexOf 15: " + ListAlgorithms.indexOf(numbers, 15));
        System.out.println("countOf 8: " + ListAlgorithms.countOf(numbers, 8));
        System.out.println("largest: " + ListAlgorithms.largest(numbers));
        System.out.println("average: " + ListAlgorithms.average(numbers));
        System.out.println("reversed: " + ListAlgorithms.reversed(numbers));
        System.out.println("random: " + ListAlgorithms.randomElement(numbers));
        System.out.println("random: " + ListAlgorithms.randomElement(numbers));
        System.out.println("random: " + ListAlgorithms.randomElement(numbers));
    }
}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListsAndMaps {
    public static void main(String[] args) {
        // Arrays have a fixed size
        int[] scores = {90, 85, 77};
        System.out.println("First score: " + scores[0] + ", length: " + scores.length);

        // Lists grow as you add to them
        List<String> fruits = new ArrayList<>();
        fruits.add("apple");
        fruits.add("banana");
        for (String fruit : fruits) {
            System.out.println("fruit: " + fruit);
        }

        // Maps store key -> value pairs
        Map<String, Integer> stock = new HashMap<>();
        stock.put("apple", 12);
        stock.put("banana", 4);
        System.out.println("apples in stock: " + stock.get("apple"));
    }
}

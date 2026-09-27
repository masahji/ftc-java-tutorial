public class ControlFlow {
    public static void main(String[] args) {
        int temperature = 22;

        if (temperature > 25) {
            System.out.println("Hot");
        } else if (temperature > 15) {
            System.out.println("Pleasant");
        } else {
            System.out.println("Cold");
        }

        for (int i = 1; i <= 5; i++) {
            System.out.println("for loop: " + i);
        }

        int n = 3;
        while (n > 0) {
            System.out.println("countdown: " + n);
            n--;
        }

        String day = "SAT";
        String kind = switch (day) {
            case "SAT", "SUN" -> "weekend";
            default -> "weekday";
        };
        System.out.println(day + " is a " + kind);
    }
}
